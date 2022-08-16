package com.bnb.doggydoo.homemodule.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bnb.doggydoo.R
import com.bnb.doggydoo.callawet.ui.DoctorDetailActivity
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentHomeBinding
import com.bnb.doggydoo.homemodule.adapter.HomeFeatureAdapter
import com.bnb.doggydoo.homemodule.adapter.HomePlayDateAdapter
import com.bnb.doggydoo.homemodule.datasource.model.home.MapParkDetail
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.login.ui.LoginActivity
import com.bnb.doggydoo.mydog.ui.PetProfileActivity
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedDetail
import com.bnb.doggydoo.newsfeed.ui.NewsfeedAdapterCustom
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.parkdescription.ui.PetParkDescription
import com.bnb.doggydoo.shelter.ui.DogShelterActivity
import com.bnb.doggydoo.sos.ui.DistressPetDetailActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


private const val TAG = "homeFragTag"

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private var currentLatLng: LatLng? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var playDateAdapter: HomePlayDateAdapter
    private var _binding: FragmentHomeBinding? = null
    private val bind get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var mMap: GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var mapStyleOptions: MapStyleOptions? = null
    private var mapStyle: String = ""
    var currentLat: String = ""
    var currentLang: String = ""
    var mcurrentLat: String = ""
    var mcurrentLang: String = ""
    var isAllFabsVisible: Boolean? = null
    var placesClient: PlacesClient? = null
    private var clieckedType: String = "allexplore"
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var newsFeedViewModel: NewsFeedViewModel
    private lateinit var adapter: NewsfeedAdapterCustom
    private var filterType: String = ""

    private var currentPolyline: Polyline? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        getLocationDetail()
        getUserStatusApi()

        initializeBottomSheetAdapters()

//       val mGoogleApiClient = GoogleApiClient.Builder(requireContext())
//            .addConnectionCallbacks(this@HomeFragment)
//            .addOnConnectionFailedListener(requireContext())
//            .addApi(LocationServices.API)
//            .build()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bind.recentre.setOnClickListener(){
            setUpMap()
            getMapDateApi(
                MyApp.getSharedPref().userLat,
                MyApp.getSharedPref().userLong,
                clieckedType
            )
            bind.Place.text=null
        }
    }

    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String? {
        // Origin of route
        val str_origin =
            "origin=" + origin.latitude.toString() + "," + origin.longitude
        // Destination of route
        val str_dest =
            "destination=" + dest.latitude.toString() + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=" + getString(
            R.string.google_map_api_key
        )
    }


    fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline?.remove()
        currentPolyline = mMap!!.addPolyline(values[0] as PolylineOptions?)
    }


    private fun getInit() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        isAllFabsVisible = false
        bind.ivUser.loadImageFromString(
            requireContext(),
            ApiConstant.PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
        )

        bind.Place.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        if (bind.llLocation.visibility == View.VISIBLE) {
            val apiKey = getString(R.string.google_map_api_key)

            if (!Places.isInitialized()) {
                Places.initialize(context, apiKey)
            }
            placesClient = Places.createClient(context)
        }

        bind.filterData.setOnClickListener {
            exploreDialog()
        }

    }

    private fun exploreDialog() {
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_explore)

        val rlType1 = dialog.findViewById<View>(R.id.rlType1) as RelativeLayout
        val rlType2 = dialog.findViewById<View>(R.id.rlType2) as RelativeLayout
        val rlType3 = dialog.findViewById<View>(R.id.rlType3) as RelativeLayout
        val rlType4 = dialog.findViewById<View>(R.id.rlType4) as RelativeLayout
        val rlType5 = dialog.findViewById<View>(R.id.rlType5) as RelativeLayout
        val rlType6 = dialog.findViewById<View>(R.id.rlType6) as RelativeLayout
        val rlType7 = dialog.findViewById<View>(R.id.rlType7) as RelativeLayout
        val rlType8 = dialog.findViewById<View>(R.id.rlType8) as RelativeLayout

        val ivType1 = dialog.findViewById<View>(R.id.ivType1) as ImageView
        val ivType2 = dialog.findViewById<View>(R.id.ivType2) as ImageView
        val ivType3 = dialog.findViewById<View>(R.id.ivType3) as ImageView
        val ivType4 = dialog.findViewById<View>(R.id.ivType4) as ImageView
        val ivType5 = dialog.findViewById<View>(R.id.ivType5) as ImageView
        val ivType6 = dialog.findViewById<View>(R.id.ivType6) as ImageView
        val ivType7 = dialog.findViewById<View>(R.id.ivType7) as ImageView
        val ivType8 = dialog.findViewById<View>(R.id.ivType8) as ImageView

        val tvConfirmType = dialog.findViewById<View>(R.id.tvConfirmType) as TextView
        val ivCancel = dialog.findViewById<View>(R.id.ivCancel) as ImageView

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }

        rlType1.setOnClickListener {
            clieckedType = "allexplore"
            ivType1.visibility = View.VISIBLE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType2.setOnClickListener {
            clieckedType = "friendlocation"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.VISIBLE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType3.setOnClickListener {
            clieckedType = "findmate"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.VISIBLE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            //  binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType4.setOnClickListener {
            clieckedType = "foster"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.VISIBLE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType5.setOnClickListener {
            clieckedType = "dogsitter"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.VISIBLE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType6.setOnClickListener {
            clieckedType = "clinic"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.VISIBLE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.GONE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }
        rlType7.setOnClickListener {
            clieckedType = "shelter"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.VISIBLE
            ivType8.visibility = View.GONE
            //  binding.bottomSheetLayout.bottomSheet.hide()
        }

        rlType8.setOnClickListener {
            clieckedType = "clinic"
            ivType1.visibility = View.GONE
            ivType2.visibility = View.GONE
            ivType3.visibility = View.GONE
            ivType4.visibility = View.GONE
            ivType5.visibility = View.GONE
            ivType6.visibility = View.GONE
            ivType7.visibility = View.GONE
            ivType8.visibility = View.VISIBLE
            // binding.bottomSheetLayout.bottomSheet.hide()
        }

        tvConfirmType.setOnClickListener {
            isAllFabsVisible = false
            dialog.dismiss()
            getMapDateApi(
                MyApp.getSharedPref().userLat,
                MyApp.getSharedPref().userLong,
                clieckedType
            )

            mMap!!.clear()
            currentLatLng = LatLng(
                MyApp.getSharedPref().userLat.toDouble(),
                MyApp.getSharedPref().userLong.toDouble()
            )
            mMap!!.addMarker(
                MarkerOptions().icon(
                    BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                ).position(currentLatLng!!).title("You")
            )
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 18f))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng!!))
        }

        dialog.show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                        bind.Place.text = place.name
                        val destinationLatLng: LatLng? = place.latLng
                        mcurrentLat = destinationLatLng?.latitude.toString()
                        mcurrentLang = destinationLatLng?.longitude.toString()

                        getMapDateApi(mcurrentLat, mcurrentLang, "allexplore")
                        MyApp.getSharedPref().userLat = mcurrentLat
                        MyApp.getSharedPref().userLong = mcurrentLang

                        mMap!!.addMarker(
                            MarkerOptions().icon(
                                BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                            ).position(destinationLatLng!!).title("You")
                        )

                        val cameraPosition = CameraPosition.Builder()
                            .target(destinationLatLng)
                            .bearing(45f)
                            .tilt(90f)
                            .zoom(18f)
                            .build()

                        mMap!!.animateCamera(
                            CameraUpdateFactory.newCameraPosition(cameraPosition),
                            3000,
                            object : CancelableCallback {
                                override fun onFinish() {
                                    //  Toast.makeText(requireContext(), "Finished", Toast.LENGTH_SHORT).show()
                                }

                                override fun onCancel() {
                                    // Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        // mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 18f))

                        mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                       // FetchURL(requireContext()).execute(getUrl(currentLatLng!!, destinationLatLng, "driving"), "driving");

//                        Log.d("TAG", "Current LatLong : "+ currentLatLng)
//                        Log.d("TAG", "Destination LatLong : "+ destinationLatLng)
//
//                        val base_url = "http://maps.googleapis.com/"
//                       // val base_url = "https://maps.googleapis.com/maps/api/directions/json?origin=${currentLatLng?.latitude},${currentLatLng?.longitude}&destination=${destinationLatLng.latitude},${destinationLatLng.longitude}&sensor=true&mode=driving&key=" + getString(R.string.google_map_api_key)+"/"
//                        val origin = currentLatLng.toString()
//                        val dest = destinationLatLng.toString()
//
//                        val retrofit = Retrofit.Builder()
//                            .baseUrl(base_url)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build()
//
//                        val service = retrofit.create(MyApi::class.java)
//                        service.getJson(origin, dest).enqueue(object : Callback<DirectionResults> {
//                            override fun onResponse(
//                                call: Call<DirectionResults>,
//                                response: Response<DirectionResults>
//                            ) {
//                                Log.d("CallBack", " response is $response")
//                                val routeA: Route = response.body()!!.getRoutes()!!.get(0)
//                                val legs = routeA.legses[0]
//                            }
//
//                            override fun onFailure(call: Call<DirectionResults>, t: Throwable) {
//                                Log.d("CallBack", " Throwable is $t")
//                            }
//                        })

//                        mMap!!.addPolyline(
//                            PolylineOptions()
//                                .add(currentLatLng)
//                                .add(destinationLatLng)
//                                .width(12f)
//                                .color(Color.BLACK)
//
//                        )

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage!!)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

//    private fun getDirectionURL(origin:LatLng, dest:LatLng) : String{
//        return "https://maps.googleapis.com/maps/api/directions/json?origin=\(source.latitude),\(source.longitude)&destination=\(destination.latitude),\(destination.longitude)&sensor=true&mode=driving&key=AIzaSyDLoE5-MCJ0Gl-q4V-5-4udXrmGiyLZxoc"
//    }

    private fun getLocationDetail() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                currentLat = location.latitude.toString()
                currentLang = location.longitude.toString()
                getMapDateApi(currentLat, currentLang, "allexplore")
            } else {
                buildAlertMessageNoGps()
                /*Toast.makeText(
                    requireContext(),
                    "Please enable location service.",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }
    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun initializeBottomSheetAdapters() {
        val name = arrayOf<String>(
            "Playdate",
            "NewsFeed",
            "Adoption",
            "SOS",
           // "DogSitting",
           //  "Fostering",
            "Find a vet",
          //  "Article",
          //  "Training"

        )
        val bgDrawableIds = intArrayOf(
            R.raw.playdate_lottie,
            R.raw.adoption_lottie,
            R.raw.newsfeed_lottie,
            R.raw.sos_doggy_lottie,
            R.raw.find_a_vet_lottie,

            R.mipmap.play_date,
            R.drawable.ic_market,
            R.mipmap.fostering,// adoption
            R.drawable.sos,
           // R.mipmap.fostering, // dog sitting
          //  R.mipmap.fostering,
            R.mipmap.calavet,
            //R.mipmap.article, // article
          //  R.mipmap.training,

            )
        bind.bottomSheetLayout.featuresRv.adapter = HomeFeatureAdapter(
            requireContext(),
            name,
            bgDrawableIds,
            this
        )

        playDateAdapter = HomePlayDateAdapter(requireContext())
        bind.bottomSheetLayout.pladateRv.adapter = playDateAdapter
        //getPlayDateApi()

        setUpNewsfeed()
    }

    private fun setUpNewsfeed() {
        getNewsData()
        getNewsFeedData()
    }


    private fun getNewsData() {
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)
       // adapter = NewsFeedDataAdapter(requireContext(), MyApp.getSharedPref().newsTypeFilter)
        adapter = NewsfeedAdapterCustom(requireContext(), MyApp.getSharedPref().newsTypeFilter) { blockUserId, type ->
            when (type) {
                "block" -> {
                    blockUser(blockUserId)
                }
                "delete" -> {
                    deletePost(blockUserId, "1")
                }
                "like" -> {
                    likePost(blockUserId, "1")
                }
                else -> {
                    likePost(blockUserId, "2")
                }
            }

        }
        bind.bottomSheetLayout.newsfeedRv.adapter = adapter


        //** Set the colors of the Pull To Refresh View
//        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.cal))
//        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
//
//        binding.itemsswipetorefresh.setOnRefreshListener {
//            binding.rvUploadedItem.clearAnimation()
//            getNewsFeedData()
//            adapter = NewsFeedDataAdapter(requireContext(), MyApp.getSharedPref().newsTypeFilter) { blockUserId, type ->
//                when (type) {
//                    "block" -> {
//                        blockUser(blockUserId)
//                    }
//                    "delete" -> {
//                        deletePost(blockUserId, "1")
//                    }
//                    "like" -> {
//                        likePost(blockUserId, "1")
//                    }
//                    else -> {
//                        likePost(blockUserId, "2")
//                    }
//                }
//
//            }
//
//            binding.rvUploadedItem.adapter = adapter
//            binding.itemsswipetorefresh.isRefreshing = false
//        }
    }

    private fun likePost(newsfeedId: String, type: String) {
        newsFeedViewModel.likeNewsFeedPost(newsfeedId, type, MyApp.getSharedPref().userId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                      //  binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                      //  binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                           // it.message?.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        getNewsFeedData()
                    }
                    Result.Status.ERROR -> {
                       // binding.progressBar.hide()
                       // it.message?.snack(Color.RED, binding.parent)
                        Log.d("TAG", "likePost: error")
                    }
                }
            }
        )
    }
    private fun getNewsFeedData() {
        filterType = when (MyApp.getSharedPref().newsTypeFilter) {
            "All" -> {
                "1"
            }
            "My Post" -> {
                "3"
            }
            else -> {
                "2"
            }
        }
        newsFeedViewModel.getByNewsFeedId(
            "1",
            MyApp.getSharedPref().userId,
            filterType
        ).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        //binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                       // binding.progressBar.hide()

                        if (it.data!!.responseCode == ("0")) {
                           // binding.noData.show()
                            return@Observer
                        }
                        renderList(it.data.newsfeeddetail)
                    }
                    Result.Status.ERROR -> {
                        Log.d("TAG", "getNewsFeedData: error")
                        //binding.progressBar.hide()
                       // it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }

    private fun renderList(data: List<NewsFeedDetail>) {
        adapter.addData(data)
    }

    private fun deletePost(newsfeedId: String, type: String) {
        newsFeedViewModel.deleteNewsFeedPost(MyApp.getSharedPref().userId,newsfeedId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        //binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        //binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                          //  it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                       // it.data.responseMessage.snack(Color.BLACK, binding.parent)
                        getNewsFeedData()
                    }
                    Result.Status.ERROR -> {
                       // binding.progressBar.hide()
                       // it.message?.snack(Color.RED, binding.parent)
                        Log.d("TAG", "deletePost: error")
                    }
                }
            }
        )
    }

    private fun blockUser(blockUserId: String) {
        newsFeedViewModel.blockNewsFeedUser(MyApp.getSharedPref().userId, blockUserId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {

                    }
                    Result.Status.SUCCESS -> {


                        if (it.data!!.responseCode == ("0")) {
                            //binding.noData.show()
                            return@Observer
                        }
                        getNewsFeedData()
                    }
                    Result.Status.ERROR -> {
                        Log.d("TAG", "blockUser: er")
                       // it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }

    private fun getUserStatusApi() {
        homeViewModel.getUserStatusResponse(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            if (it.data!!.responseCode == "0") {
                                startActivity(
                                    Intent(requireContext(), LoginActivity::class.java).setFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    )
                                )
                                return@Observer
                            }
                        }
                        Result.Status.ERROR -> {
                            it.message!!.snack(Color.RED, bind.root)
                        }
                    }
                })
    }

    private fun getPlayDateApi() {
        homeViewModel.getUpcomingPlayDateResponse(
            MyApp.getSharedPref().userId,
            currentLat,
            currentLang
        )
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            if (it.data!!.responseCode == "0") {
                                //bind.bottomSheetLayout.noPlayDate.show()
                                return@Observer
                            }
                            if (it.data.parkPlayDate.isEmpty()) {
                                //bind.bottomSheetLayout.noPlayDate.show()
                                return@Observer
                            }
                            //bind.bottomSheetLayout.noPlayDate.hide()
                            playDateAdapter.submitList(it.data.parkPlayDate)
                        }
                        Result.Status.ERROR -> {
                            it.message!!.snack(Color.RED, bind.root)
                        }
                    }
                })
    }

    private fun getMapDateApi(currentLat: String, currentLang: String, type: String) {
        homeViewModel.getHomeMapResponse(
            MyApp.getSharedPref().userId,
            currentLat,
            currentLang,
            type
        )
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            if (it.data!!.responseCode == "0") {
                                return@Observer
                            }
                            // mMap!!.clear()
                            setMarker(it.data.ParkDetailList)
                        }
                        Result.Status.ERROR -> {
                        }
                    }
                })
    }

    private fun setMarker(parkDetailList: List<MapParkDetail>) {
        for (i in parkDetailList.indices) {
            if ((parkDetailList[i].park_lattitute != "null") && (parkDetailList[i].park_longitute != "null")) {

                if (parkDetailList[i].response_type == "park") {
                    mMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                parkDetailList[i].park_lattitute.toDouble(),
                                parkDetailList[i].park_longitute.toDouble()
                            )
                        )
                            .title(parkDetailList[i].park_name + "," + parkDetailList[i].response_type)
                            .snippet(parkDetailList[i].parkid)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.park_icon))
                    )
                } else if (parkDetailList[i].response_type == "user") {
                    mMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                parkDetailList[i].park_lattitute.toDouble(),
                                parkDetailList[i].park_longitute.toDouble()
                            )
                        )
                            .title(parkDetailList[i].park_name + "," + parkDetailList[i].response_type)
                            .snippet(parkDetailList[i].parkid)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.fostering_icon))
                    )
                } else if (parkDetailList[i].response_type == "pet") {
                    if (parkDetailList[i].type == "distresspet") {
                        mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    parkDetailList[i].park_lattitute.toDouble(),
                                    parkDetailList[i].park_longitute.toDouble()
                                )
                            )
                                .title("Pinned" + "," + "distress pet")
                                .snippet(parkDetailList[i].parkid)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sos_pin))
                        )
                    } else {
                        mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    parkDetailList[i].park_lattitute.toDouble(),
                                    parkDetailList[i].park_longitute.toDouble()
                                )
                            )
                                .title(parkDetailList[i].park_name + "," + parkDetailList[i].response_type)
                                .snippet(parkDetailList[i].parkid)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pet_icon))
                        )
                    }

                } else if (parkDetailList[i].response_type == "clinic") {
                    mMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                parkDetailList[i].park_lattitute.toDouble(),
                                parkDetailList[i].park_longitute.toDouble()
                            )
                        )
                            .title(parkDetailList[i].park_name + "," + parkDetailList[i].response_type)
                            .snippet(parkDetailList[i].parkid)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.clinic_icon))
                    )
                } else {
                    mMap!!.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                parkDetailList[i].park_lattitute.toDouble(),
                                parkDetailList[i].park_longitute.toDouble()
                            )
                        )
                            .title(parkDetailList[i].park_name + "," + parkDetailList[i].response_type)
                            .snippet(parkDetailList[i].parkid)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.shelter_icon_map))
                    )
                }

            }
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(18f))
        }

        mMap!!.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            @SuppressLint("SetTextI18n")
            override fun getInfoContents(marker: Marker): View? {
                val v = View.inflate(requireContext(), R.layout.single_snap_user, null)
                // set widget of your custom_layout like below
                val parkName: TextView = v.findViewById(R.id.userName) as TextView
                val parkAddress: TextView = v.findViewById(R.id.userDistance) as TextView
                val parkadd: TextView = v.findViewById(R.id.userDistance1) as TextView
                parkName.text = marker.title

                val geocoder = Geocoder(context, Locale.getDefault())

                val addresses: List<Address> = geocoder.getFromLocation(
                    marker.position.latitude,
                    marker.position.longitude,
                    1
                )
                if(addresses.isNotEmpty()) {
                    val address: String? = addresses[0].getAddressLine(0)
                    val city: String? = addresses[0].getLocality()
                    val state: String? = addresses[0].getAdminArea()
                    val zip: String? = addresses[0].getPostalCode()
                    val country: String? = addresses[0].getCountryName()
                    try {
                        parkAddress.text = "$city, $state"
                        parkadd.text = "$country, $zip"
                    } catch (e: Exception) {
                    }
                }
                return v
            }
        })


        mMap!!.setOnMarkerClickListener { marker ->
            val markerName = marker.title
            val markerId = marker.snippet
            val parlLat = marker.position.latitude
            val parkLong = marker.position.longitude


            if (markerName == "You") {
                Toast.makeText(
                    requireContext(),
                    "Please click on other location to see the detail.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val value = markerName!!.split(",").toTypedArray()
                val type: String = value[1]

                mMap!!.setOnInfoWindowClickListener {
                    when (type) {
                        "user" -> {
                            startActivity(
                                Intent(requireContext(), UserProfileActivity::class.java)
                                    .putExtra("viewuserid", markerId)
                                    .putExtra("from", "map")
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }
                        "park" -> {
                            startActivity(
                                Intent(requireContext(), PetParkDescription::class.java)
                                    .putExtra("parkid", markerId)
                                    .putExtra("myLat", parlLat)
                                    .putExtra("myLong", parkLong)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }
                        "pet" -> {
                            startActivity(
                                Intent(requireContext(), PetProfileActivity::class.java)
                                    .putExtra("petId", markerId)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )

                        }
                        "clinic" -> {
                            startActivity(
                                Intent(requireContext(), DoctorDetailActivity::class.java)
                                    .putExtra("from", "nearbyHos")
                                    .putExtra("id", markerId)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }

                        "shelter" -> {
                            startActivity(
                                Intent(requireContext(), DogShelterActivity::class.java)
                                    .putExtra("title", markerName)
                                    .putExtra("shelter_id", markerId)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }

                        "distress pet" -> {
                            startActivity(
                                Intent(requireContext(), DistressPetDetailActivity::class.java)
                                    .putExtra("id", markerId)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }

                    }

                }
            }

            false
        }

    }
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = false
        mMap!!.setOnMarkerClickListener(this)
        mMap!!.uiSettings.isMyLocationButtonEnabled = false

        // mMap!!.getUiSettings().setMyLocationButtonEnabled(false);
        mapStyle = MyApp.getSharedPref().userReqType
        if (mapStyle.isEmpty()) {
            mapStyle = "Retro"
        }
        if (mapStyle == "Standard") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.standard_style)
        } else if (mapStyle == "Silver") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.silver_style)
        } else if (mapStyle == "Retro") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.retro_style)
        } else if (mapStyle == "Dark") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.dark_style)
        } else if (mapStyle == "Night") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.night_style)
        } else if (mapStyle == "Aubergine") {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.auber_style)
        } else {
            mapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.standard_style)
        }
        googleMap.setMapStyle(mapStyleOptions)
        setUpMap()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap!!.clear()
        mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isCompassEnabled = false

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                MyApp.getSharedPref().userLat = location.latitude.toString()
                MyApp.getSharedPref().userLong = location.longitude.toString()
                mMap!!.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    ).position(currentLatLng!!).title("You")
                )
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 18f))
                val zoomLevel = 18.0f //This goes up to 21
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, zoomLevel))
               // mMap!!.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng!!))
            }
        }

    }

    fun onSosClicked() {
        requireView().findNavController().navigate(R.id.action_nav_home_to_SOSDistressFragment)
    }

}


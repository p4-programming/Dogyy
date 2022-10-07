package com.bnb.doggydoo.sos.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.FragmentPinMapBinding
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.network.ApiConstant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


class Pin_Map_Fragment : Fragment(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentPinMapBinding? = null
    private val binding get() = _binding!!
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var currentLatLng: LatLng? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    var placesClient: PlacesClient? = null
    var isAllFabsVisible: Boolean? = null
    var destinationLatLng: LatLng? = null
    var apiKey: String = ""
    private val args: Pin_Map_FragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPinMapBinding.inflate(inflater, container, false)
        val view = binding.root

        CommonMethod.makeTransparentStatusBar(activity?.window)

        binding.ivBack.setOnClickListener {
            requireView().findNavController().popBackStack()
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        val ai: ApplicationInfo = context!!.packageManager
            .getApplicationInfo(context!!.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        apiKey = value.toString()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.Place.text=null
    }

    private fun getInit() {
        isAllFabsVisible = false
        binding.Place.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        if (binding!!.llLocation.visibility == View.VISIBLE) {
            val apiKey = getString(R.string.google_map_api_key)

            if (!Places.isInitialized()) {
                Places.initialize(context, apiKey)
            }
            placesClient = Places.createClient(context)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        mMap!!.clear()
                        Log.d("Deepak", "Place: ${place.name}, ${place.id}")
                        binding.Place.text = place.name
                        Log.d("Deepak", "Place : " + binding.Place.text)
                        destinationLatLng = place.latLng
                        //mcurrentLat = destinationLatLng?.latitude.toString()
                        //mcurrentLang = destinationLatLng?.longitude.toString()

                        val destinationLocation = LatLng(
                            MyApp.getSharedPref().userLat.toDouble(),
                            MyApp.getSharedPref().userLong.toDouble()
                        )

                        val originLocation = place.latLng
//                        mMap!!.addMarker(MarkerOptions().position(originLocation))
//                        mMap!!.addMarker(MarkerOptions().position(destinationLocation))
                        //val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
                       // GetDirection(urll).execute()
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
                    }
                }
            }
        }
    }



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = false
        mMap!!.uiSettings.isMyLocationButtonEnabled = false
        mMap!!.setOnMarkerClickListener(this)
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.standard_style))
        setUpMap()
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
                Pin_Map_Fragment.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap!!.isMyLocationEnabled = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                mMap!!.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory.fromResource(R.mipmap.pin_marker)
                    ).position(currentLatLng!!).title("You")
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ) // Sets the center of the map to location user
                    .zoom(17f) // Sets the zoom
                    .bearing(0f) // Sets the orientation of the camera to east
                    .tilt(40f) // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder

                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


                /*mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 20f))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng!!))*/
            }
        }
        binding.llLocation.hide()
        mMap!!.setOnMapClickListener {
            mMap!!.clear()
            currentLatLng = LatLng(it.latitude, it.longitude)
            // Toast.makeText(this, "Pin a location on map=="+ currentLatLng, Toast.LENGTH_SHORT).show()
            mMap!!.addMarker(
                MarkerOptions().icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.pin_marker)
                ).position(currentLatLng!!).title("Pin location")
            )
            showPinOption(
                it.latitude.toString(),
                it.longitude.toString()
            )
        }

        mMap!!.setOnMarkerClickListener { marker ->
            mMap!!.setOnInfoWindowClickListener {
                if (currentLatLng != null) {
                    binding.llLocation.hide()
                    confirmPinOption(
                        marker.position.latitude.toString(),
                        marker.position.longitude.toString()
                    )
                } else {
                    Toast.makeText(requireContext(), "Pin a location on map.", Toast.LENGTH_SHORT).show()
                }

            }
            false
        }
    }

    private fun showPinOption(lat: String, longg: String) {
        binding.llLocation.show()
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        addresses = geocoder.getFromLocation(
            lat.toDouble(),
            longg.toDouble(),
            1
        )

        if(!addresses.isNullOrEmpty()){
            val address: String = addresses[0].getAddressLine(0)
            binding.tvAddress.text = address
        }
    }

    private fun confirmPinOption(lat: String, longg: String) {
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirm_pin_option)

        val cancel = dialog.findViewById<View>(R.id.tvCancel) as TextView
        val confirm = dialog.findViewById<View>(R.id.tvConfirm) as TextView

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        confirm.setOnClickListener {

//            val bundle = Bundle()
//            bundle.putString("latitude", lat)
//            bundle.putString("longitude", longg)
//            val fragobj = supportFragmentManager.findFragmentById(R.id.SOSMainFragment)
//            if (fragobj != null) {
//                fragobj.setArguments(bundle)
//            }

            val petDescription = args.description
            val distressType = args.type
            val uri = args.imageUri
            val notificationType = args.notificationType

            val action = Pin_Map_FragmentDirections.actionPinMapFragmentToSOSMainFragment(lat,longg,petDescription,distressType,uri.toString(),notificationType)
            requireView().findNavController().navigate(action)

//            startActivity(
//                Intent(this, ConfirmsoslocationActivity::class.java)
//                    .putExtra("pin_lat", lat)
//                    .putExtra("pin_long", longg)
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
            dialog.dismiss()
        }
        dialog.show()
    }
}

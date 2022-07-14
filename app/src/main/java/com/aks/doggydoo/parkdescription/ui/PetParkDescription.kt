package com.aks.doggydoo.parkdescription.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityPetParkDescriptionBinding
import com.aks.doggydoo.parkdescription.adapter.OverlapPeopleAdapter
import com.aks.doggydoo.parkdescription.adapter.PlaceReviewAdapter
import com.aks.doggydoo.parkdescription.datasource.model.ParkDetail
import com.aks.doggydoo.parkdescription.utility.OverlappingItem
import com.aks.doggydoo.parkdescription.viewmodel.PetDescriptionViewModel
import com.aks.doggydoo.rateplace.ui.RatePlaceActivity
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PetParkDescription : AppCompatActivity() {
    private lateinit var binding: ActivityPetParkDescriptionBinding
    private lateinit var adapter: PlaceReviewAdapter
    private val petDescriptionViewModel: PetDescriptionViewModel by viewModels()
    private var parkId: String = ""
    private var currentLat: String = ""
    private var currentLong: String = ""
    private var checkInStatus: String =""
    private var parkOpenStatus: String =""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetParkDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        showRatingList()

        binding.ratingLayout.setOnClickListener {
            Intent(
                this,
                RatePlaceActivity::class.java
            ).putExtra("partId", parkId).apply {
                resultLauncher.launch(this)
            }
        }

        binding.navIcon.setOnClickListener {
            if (parkOpenStatus == "1"){
                if (checkInStatus == "0"){
                    checkInCheckOutAPI("in")
                }else{
                    checkInCheckOutAPI("out")
                }
            }else{
                Toast.makeText(this,"Sorry, Park is closed now.", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun getInit() {
        parkId = intent.getStringExtra("parkid").toString()
        currentLat = intent.getStringExtra("myLat").toString()
        currentLong = intent.getStringExtra("myLong").toString()

        System.out.println("park id>>"+ parkId)

        binding.overlapTotalPeopleRv.addItemDecoration(OverlappingItem())
        binding.overlapTotalPeopleRv.adapter = OverlapPeopleAdapter()
        adapter = PlaceReviewAdapter(this)
        binding.reviewRv.adapter = adapter


        binding.back.setOnClickListener {
            finish()
        }

        binding.navigateMe.setOnClickListener {
            val builder: Uri.Builder = Uri.Builder()
            builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter(
                    "destination",
                    currentLat.toString() + "," + currentLong.toString()
                )
            val url: String = builder.build().toString()
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showRatingList()
                }
            }
        }

    private fun showRatingList() {
        petDescriptionViewModel.getParkDescriptionData(parkId,MyApp.getSharedPref().userId).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    if (it.data?.responseCode.equals("0")) {
                        binding.progressBar.show()
                        it.data?.responseMessage?.snack(Color.RED, binding.parent)
                        return@Observer
                    }
                    binding.progressBar.hide()
                    binding.rating.text = it.data?.parkRate.toString()
                    adapter.submitList(it.data?.parkreviewdetail)
                    it.data?.parkdetail?.let { it1 -> setDataInUI(it1) }
                }
                Result.Status.ERROR -> {
                    binding.progressBar.show()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }

    /**
     * parkDetail[0] the size of the list will always remain the same
     */
    private fun setDataInUI(parkDetail: List<ParkDetail>) {
        checkInStatus = parkDetail[0].checkin_status
        binding.placeName.text = parkDetail[0].place_name
        binding.descriptionText.text = parkDetail[0].place_description
        binding.totalPeople.text = parkDetail[0].total_park_user_count + " " + "people are here"
        binding.expandedImage.loadImageFromString(
            this,
            ApiConstant.PLACE_IMAGE_BASE_URL + parkDetail[0].parkImageList[0].image
        )
        if (parkDetail[0].zone == "1") { //checks if place is pet friendly or not "1" for yes & "0" for no
            binding.petFriendlyText.text = "Pet Friendly"
        } else {
            binding.petFriendlyText.text = "Not Pet Friendly"
        }

        if (parkDetail[0].park_close_open == "1") {
            parkOpenStatus ="1"
            binding.openNow.text = "Open Now"
            binding.ivStatus.setImageResource(R.drawable.open_now)
        }else{
            parkOpenStatus ="2"
            binding.openNow.text = "Closed"
            binding.ivStatus.setImageResource(R.drawable.close_now)
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time1: String = parkDetail[0].open_time + ":00.000"
            val time2: String = parkDetail[0].close_time + ":00.000"
            val fromTime = LocalTime.parse(time1)
            val toTime = LocalTime.parse(time2)
            val currentTime = LocalTime.now()

            if (currentTime.isBefore(toTime)) {
                binding.openNow.text = "Open Now"
                binding.ivStatus.setImageResource(R.drawable.open_now)
            }else{
                binding.openNow.text = "Closed"
                binding.ivStatus.setImageResource(R.drawable.close_now)
            }
        }
*/
        getFullAddress(
            parkDetail[0].vat_latitude.toDouble(),
            parkDetail[0].vat_longitude.toDouble()
        )
    }

    private fun getFullAddress(latitude: Double, longitude: Double) {
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )
        val address: String = addresses[0].getAddressLine(0)
        binding.placeAddress.text = address
    }

    private fun checkInCheckOutAPI(status: String) {
        petDescriptionViewModel.getCheckInData(status,parkId,MyApp.getSharedPref().userId).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.responseMessage?.snack(Color.BLACK, binding.parent)

                    if (it.data?.responseCode.equals("0")) {
                        binding.progressBar.show()
                        it.data?.responseMessage?.snack(Color.RED, binding.parent)
                        return@Observer
                    }
                }
                Result.Status.ERROR -> {
                    binding.progressBar.show()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }
}

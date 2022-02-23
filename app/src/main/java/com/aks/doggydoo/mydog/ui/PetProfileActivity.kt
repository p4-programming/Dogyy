package com.aks.doggydoo.mydog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityOtherDogProfileBinding
import com.aks.doggydoo.mydog.adapter.ImageViewPagerAdapter
import com.aks.doggydoo.mydog.adapter.MyPetDocumentAdapter
import com.aks.doggydoo.mydog.adapter.MyPetReminder
import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.aks.doggydoo.mydog.viewmodel.MyDogViewModel
import com.aks.doggydoo.playdate.ui.SelectTimeDateActivity
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetProfileActivity : AppCompatActivity() {
    private var petId: String = ""
    private lateinit var binding: ActivityOtherDogProfileBinding
    private lateinit var bitmap: Bitmap
    private lateinit var petDocumentAdapter: MyPetDocumentAdapter
    private lateinit var petReminder: MyPetReminder
    private lateinit var adapter: ImageViewPagerAdapter

    //initialized view model here
    private lateinit var myDogViewModel: MyDogViewModel
    private var reminder = ArrayList<String>()
    private var reminderName = ArrayList<String>()
    private var reminderTime = ArrayList<String>()
    private var reminderid: String = ""
    private var flagStatus: Int = 0
    private var petName: String = ""
    private var petImage: String = "";
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherDogProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callGetDogDescriptionAPI()
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        petId = intent.getStringExtra("petId").toString()


        /* val viewPager = binding.sliderviewpager
         adapter = ImageViewPagerAdapter(this)
         viewPager.adapter = adapter
         binding.dotsIndicator.setViewPager2(viewPager)*/


        binding.backButton.setOnClickListener { finish() }
        binding.apply {
            rlReqPlayDate.setOnClickListener {
                startActivity(
                    Intent(this@PetProfileActivity, SelectTimeDateActivity::class.java)
                        .putExtra("receiveId", userId)
                        .putExtra("from","other")
                        .putExtra("petId", petId)
                )
                finish()
            }
        }

    }


    private fun callGetDogDescriptionAPI() {
        System.out.println("ped id>>" + petId.toString())
        System.out.println("ped id>>" + MyApp.getSharedPref().userId)
        myDogViewModel.getPetDescriptionData(petId, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        setDataInUI(it.data!!)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(data: PetDescriptionResponse) {
        petName = data.petdetail.pet_name
        petId = data.petdetail.id
        userId = data.userdetail.id
        binding.petName.text = data.petdetail.pet_name + "," + data.petdetail.pet_gender
        binding.breedAge.text =
            "${data.petdetail.breed}, ${data.petdetail.pet_age} year, ${data.petdetail.pet_age_month} month old"

        if (data.petdetail.pet_gender == "male") {
            binding.ivGender.setImageResource(R.drawable.gender_male)
        } else {
            binding.ivGender.setImageResource(R.drawable.gender_female)
        }

        if (data.petdetail.is_pet_vaccinated == "yes") {
            binding.height.text = "Vaccinated"
            binding.ivVaccination.setImageResource(R.drawable.green_verified_tick)
        } else {
            binding.height.text = "Not vaccinated"
            binding.ivVaccination.setImageResource(R.drawable.yellow_verified_tick)
        }

        //binding.height.text = data.petdetail.is_pet_vaccinated
        binding.gender.text = data.petdetail.pet_gender
        binding.weight.text = data.petdetail.pet_weight + "Kg"
        binding.descriptionText.text = data.petdetail.pet_description
        flagStatus = 0
        if (data.petImage.size > 0) {
            petImage = ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
            binding.dogImage.loadImageFromString(
                this,
                ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
            )
            binding.dogImage.show()
        } else {
            binding.dogImage.setImageResource(R.drawable.dummy_pet)
        }
    }

    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
    }
}
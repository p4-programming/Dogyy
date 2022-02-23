package com.aks.doggydoo.postadoptiondetail.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.adoptionrequestsent.ui.AdoptionRequestSend
import com.aks.doggydoo.commonutility.*
import com.aks.doggydoo.databinding.ActivityPostAdoptionBinding
import com.aks.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.aks.doggydoo.postadoptiondetail.viewmodel.PostAdoptionViewModel
import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody


@AndroidEntryPoint
class PostAdoption : AppCompatActivity() {

    private var picList = ArrayList<MultipartBody.Part>()
    private var userPicList = ArrayList<MultipartBody.Part>()

    private lateinit var binding: ActivityPostAdoptionBinding
    private val postAdoptionViewModel: PostAdoptionViewModel by viewModels()
    private lateinit var breedArrayAdapter: ArrayAdapter<String>
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()


    private var clickedImage = "pet1"

    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAdoptionBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener {
            finish()
        }
        setBreedList()
        binding.postAdoption.setOnClickListener {
            CommonFunctions.hideKeyBoard(it, this)
            if (validate()) {
                postAdoptionApi()
            }
        }
        binding.ageMonth.setOnClickListener {
            if (binding.ageMonth.text == "years") {
                binding.ageMonth.text = "months"
            } else {
                binding.ageMonth.text = "years"
            }
        }
        binding.weightType.setOnClickListener {
            if (binding.weightType.text == "Kgs") {
                binding.weightType.text = "Lbs"
            } else {
                binding.weightType.text = "Kgs"
            }
        }
        binding.petPicText1.setOnClickListener {
            clickedImage = "pet1"
            openGallery()
        }
        binding.petPicText2.setOnClickListener {
            clickedImage = "pet2"
            openGallery()
        }
        binding.petPicText3.setOnClickListener {
            clickedImage = "pet3"
            openGallery()
        }
        binding.userPicText1.setOnClickListener {
            clickedImage = "user1"
            openGallery()
        }
        binding.userPicText2.setOnClickListener {
            clickedImage = "user2"
            openGallery()
        }
        binding.userPicText3.setOnClickListener {
            clickedImage = "user3"
            openGallery()
        }
        setContentView(binding.root)
    }

    private fun validate(): Boolean {

        when {
            TextUtils.isEmpty(binding.name.text) -> {
                "Enter Pet Name".showToast(this)
                return false
            }
            TextUtils.isEmpty(binding.age.text) -> {
                "Enter Pet's Age".showToast(this)
                return false
            }
            TextUtils.isEmpty(binding.about.text) -> {
                "Enter Pet's Description".showToast(this)
                return false
            }
            TextUtils.isEmpty(binding.yourName.text) -> {
                "Enter Your Name".showToast(this)
                return false
            }
            TextUtils.isEmpty(binding.yourEmail.text) -> {
                "Enter Your Email".showToast(this)
                return false
            }
            TextUtils.isEmpty(binding.yourPhone.text) -> {
                "Enter Your Mobile Number".showToast(this)
                return false
            }
            binding.yourPhone.text.length < 10 -> {
                "Enter Valid Mobile Number".showToast(this)
                return false
            }
            picList.isEmpty() -> {
                "Upload Pet Image".showToast(this)
                return false
            }
            userPicList.isEmpty() -> {
                "Upload User Image".showToast(this)
                return false
            }

            else -> {
                return true
            }
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, data!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        if (clickedImage.contains("pet")) {
                            picList.add(MultipartFile.prepareFilePart(this, "photoFile[]", data))
                        } else {
                            userPicList.add(
                                MultipartFile.prepareFilePart(
                                    this,
                                    "userImageFile[]",
                                    data
                                )
                            )
                        }
                        when (clickedImage) {
                            "pet1" -> {
                                binding.petPic1.setImageBitmap(bitmap)
                                binding.petPicText1.hide()
                                binding.petPic1.show()
                            }
                            "pet2" -> {
                                binding.petPic2.setImageBitmap(bitmap)
                                binding.petPicText2.hide()
                                binding.petPic2.show()
                            }
                            "pet3" -> {
                                binding.petPic3.setImageBitmap(bitmap)
                                binding.petPicText3.hide()
                                binding.petPic3.show()
                            }
                            "user1" -> {
                                binding.userPic1.setImageBitmap(bitmap)
                                binding.userPicText1.hide()
                                binding.userPic1.show()
                            }
                            "user2" -> {
                                binding.userPic2.setImageBitmap(bitmap)
                                binding.userPicText2.hide()
                                binding.userPic2.show()
                            }
                            "user3" -> {
                                binding.userPic3.setImageBitmap(bitmap)
                                binding.userPicText3.hide()
                                binding.userPic3.show()
                            }
                        }
                    } else {
                        when (clickedImage) {
                            "pet1" -> {
                                binding.petPic1.setImageURI(result.data?.data)
                                binding.petPicText1.hide()
                                binding.petPic1.show()
                            }
                            "pet2" -> {
                                binding.petPic2.setImageURI(result.data?.data)
                                binding.petPicText2.hide()
                                binding.petPic2.show()
                            }
                            "pet3" -> {
                                binding.petPic3.setImageURI(result.data?.data)
                                binding.petPicText3.hide()
                                binding.petPic3.show()
                            }
                            "user1" -> {
                                binding.userPic1.setImageURI(result.data?.data)
                                binding.userPicText1.hide()
                                binding.userPic1.show()
                            }
                            "user2" -> {
                                binding.userPic2.setImageBitmap(bitmap)
                                binding.userPicText2.hide()
                                binding.userPic2.show()
                            }
                            "user3" -> {
                                binding.userPic3.setImageBitmap(bitmap)
                                binding.userPicText3.hide()
                                binding.userPic3.show()
                            }
                        }
                    }
                }
            }
        }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }

    private fun postAdoptionApi() {
        var vaccinated = "No"
        if (binding.yesBox.isChecked) {
            vaccinated = "Yes"
        }
        val breedId = breedIdList[breed.indexOf(binding.breedSp.selectedItem.toString())]

        postAdoptionViewModel.getPostAdoptionData(
            binding.name.text.toString().trim(),
            binding.age.text.toString().trim(),
            binding.medicalCondition.text.toString().trim(),
            breedId,
            binding.about.text.toString().trim(),
            vaccinated,
            picList,
            MyApp.getSharedPref().userId,
            binding.yourName.text.toString().trim(),
            binding.yourEmail.text.toString().trim(),
            binding.yourPhone.text.toString().trim(),
            userPicList,
            binding.ageMonth.text.toString().trim(), "1", "shelter"
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.postAdoption.isEnabled = false
                    }
                    Result.Status.SUCCESS -> {
                        binding.postAdoption.isEnabled = true
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        "Posted".snack(R.color.docbuton, binding.parent)
                        startActivity(Intent(this, AdoptionRequestSend::class.java))
                        finish()
                    }
                    Result.Status.ERROR -> {
                        binding.postAdoption.isEnabled = true
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private var breed = ArrayList<String>()
    private var breedIdList = ArrayList<String>()
    private fun setBreedList() {
        onBoardingViewModel.getPetBreedData().observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode.equals("0")) {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    for (category in it.data?.petbreeddetails!!) {
                        breed.add(category.category)
                        breedIdList.add(category.id)
                    }
                    breedArrayAdapter =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, breed)
                    breedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.breedSp.adapter = breedArrayAdapter
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }
}
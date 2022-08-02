package com.bnb.doggydoo.onboarding.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityUploadPetimageBinding
import com.bnb.doggydoo.homemodule.ui.HomeActivity
import com.bnb.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetImageUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPetimageBinding
    private var uri: Uri? = null
    private val REQUEST_PERMISSION = 100
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private var petName: String = ""
    private var petAgeYear: String = ""
    private var petAgeMonth: String = ""
    private var petBreedId: String = ""
    private var petGender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadPetimageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CommonMethod.makeTransparentStatusBar(window)
        getInit()
    }

    private fun getInit() {
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
        petName = intent.getStringExtra("pet_name").toString()
        petAgeYear = intent.getStringExtra("pet_ageyear").toString()
        petAgeMonth = intent.getStringExtra("pet_agemonth").toString()
        petBreedId = intent.getStringExtra("pet_breedid").toString()
        petGender = intent.getStringExtra("pet_gender").toString()

        binding.button.setOnClickListener {
            dogRegistrationApi()
        }

        binding.underlyingText.setOnClickListener {
            dogRegistrationApi()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.getImageFromGallery.setOnClickListener {
            ChooseOption()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun ChooseOption() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.choose_image_option)

        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val camera = dialog.findViewById<View>(R.id.ivCamera) as ImageView
        val gallery = dialog.findViewById<View>(R.id.ivGallery) as ImageView

        camera.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(500)
                .start()
            dialog.dismiss()
        }

        gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                this.type = "image/*"
                resultLauncher.launch(this)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION
            )
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    uri = result.data?.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source =
                            ImageDecoder.createSource(contentResolver, uri!!)
                        binding.getImageFromGallery.setImageBitmap(ImageDecoder.decodeBitmap(source))
                    } else {
                        binding.getImageFromGallery.setImageURI(uri)
                    }
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val imageuri: Uri = data?.data!!
            uri = imageuri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source =
                    ImageDecoder.createSource(contentResolver, imageuri)
                binding.getImageFromGallery.setImageBitmap(ImageDecoder.decodeBitmap(source))
            } else {
                binding.getImageFromGallery.setImageURI(uri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun dogRegistrationApi() {
        onBoardingViewModel.getDogData(
            MyApp.getSharedPref().userId,
            petName,
            petAgeYear,
            petAgeMonth,
            "0",
            petBreedId,
            petGender,
            "0",
            "0",
            "0",
            "",
            "",
            "-",
            MyApp.getSharedPref().userLat,
            MyApp.getSharedPref().userLong,
            MultipartFile.prepareFilePart(this@PetImageUploadActivity, "pet_image[]", uri)
        ).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    // set data
                    it.data.responseMessage.snack(Color.BLACK, binding.root)
                    startActivity(
                        Intent(
                            this,
                            HomeActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message!!.snack(Color.RED, binding.root)
                }
            }
        })


    }


    override fun onBackPressed() {
        finish()
    }
}
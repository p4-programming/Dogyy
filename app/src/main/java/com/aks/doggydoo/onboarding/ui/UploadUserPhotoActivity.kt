package com.aks.doggydoo.onboarding.ui

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
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityUploadUserphotoBinding
import com.aks.doggydoo.onboarding.datasource.model.user.UserRegistrationResponse
import com.aks.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadUserPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadUserphotoBinding
    private var uri: Uri? = null
    private val REQUEST_PERMISSION = 100
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    var name:String =""
    var username:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadUserphotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
        name = intent.getStringExtra("name").toString()
        username = intent.getStringExtra("username").toString()

        binding.button.setOnClickListener{
            registerUserAPI()
        }

        binding.underlyingText.setOnClickListener{
            registerUserAPI()
        }

        binding.getImageFromGallery.setOnClickListener {
            ChooseOption()
        }

        binding.ivBack.setOnClickListener {
            finish()
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
                .maxResultSize(500, 500)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
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

    private fun registerUserAPI() {
        onBoardingViewModel.getUserData(
            MyApp.getSharedPref().userId,
            name,
            username,
            "0",
            "0",
            MultipartFile.prepareFilePart(this@UploadUserPhotoActivity, "user_profile", uri)
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
                    saveData(it.data)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message!!.snack(Color.RED, binding.root)
                }

            }
        })

    }

    private fun saveData(data: UserRegistrationResponse?) {
        CommonMethod.showSnack(binding.root,"User registered successfully!!")
        MyApp.getSharedPref().userId = data?.user_id
        MyApp.getSharedPref().userName = data?.uname
        MyApp.getSharedPref().userImage = data?.profile_pic
        MyApp.getSharedPref().userMobile = data?.mobile
        MyApp.getSharedPref().userIdName = data?.username

        startActivity(Intent(this, DoYouhavePetActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}
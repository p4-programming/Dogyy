package com.aks.doggydoo.mydog.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aks.doggydoo.databinding.ActivityAddDocumentBinding
import com.aks.doggydoo.newsfeed.ui.UploadPhotoActivity
import com.github.dhaval2404.imagepicker.ImagePicker


class AddDocument : AppCompatActivity() {
    private lateinit var binding: ActivityAddDocumentBinding

    private var latestTmpUri: Uri? = null
    private var petId: String? = null
    private val REQUEST_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        petId = intent.getStringExtra("petId")

        binding.rlCamera.setOnClickListener {
            openCamera()
        }
        binding.galleryLayout.setOnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
            else{
                openGallery()
            }
        }
        binding.driveLayout.setOnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
            else{
                openDrive()
            }

        }


        binding.dropboxLayout.setOnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
            else{
                openDrive()
            }

        }


        binding.backButton.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val uri = result.data?.data
                    openYourActivity(uri)
                }
            }
        }



    private fun openYourActivity(uri: Uri?) {
        startActivity(
            Intent(this, UploadPhotoActivity::class.java)
                .putExtra("uri", uri.toString())
                .putExtra("petId", petId)
                .putExtra("type", "image").setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
        )
    }


    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }

    private fun openDrive() {
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                // The image was saved into the given latestTmpUri -> do something with it
                latestTmpUri?.let { postPicture(it) }
            }
        }

    private fun postPicture(uri: Uri) {
        startActivity(
            Intent(this, UploadPhotoActivity::class.java)
                .putExtra("uri", uri.toString())
                .putExtra("petId", petId).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
        )
    }

    private fun openCamera() {
        ImagePicker.with(this)
            .cameraOnly()
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(300)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(300, 300)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            //imgProfile.setImageURI(fileUri)
            postPicture(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
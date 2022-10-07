package com.bnb.doggydoo.sos.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityConfirmsoslocationBinding
import com.bnb.doggydoo.homemodule.ui.HomeActivity
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmsoslocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmsoslocationBinding
    private var pinLatitude: String = ""
    private var pinLongitude: String = ""
    private var type: String = ""
    private var notificationType: String = ""
    private val myDogViewModel: MyDogViewModel by viewModels()
    private val REQUEST_PERMISSION = 100
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmsoslocationBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(binding.root)
        getInit()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun getInit() {
        pinLatitude = intent.getStringExtra("pin_lat").toString()
        pinLongitude = intent.getStringExtra("pin_long").toString()
        binding.llUploadPic.show()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvCancel.setOnClickListener {
            startActivity(
                Intent(this@ConfirmsoslocationActivity, SOSIntro::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.tvConfirm.setOnClickListener {
            if (binding.etPetDescription.text.isEmpty()) {
                Toast.makeText(this, "Please add description.", Toast.LENGTH_SHORT).show()
            } else if (pinLatitude.isEmpty() || pinLongitude.isEmpty()) {
                Toast.makeText(this, "Please pin location again.", Toast.LENGTH_SHORT).show()
            } else {
                addDistressPetAPI()
            }
        }

        binding.ivDog.setOnClickListener {
            ChooseOption()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
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
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(500)            //Final image size will be less than 1 MB(Optional) //Final image resolution will be less than 1080 x 1080(Optional)
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

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    uri = result.data?.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source =
                            ImageDecoder.createSource(contentResolver, uri!!)
                        binding.ivDog.setImageBitmap(ImageDecoder.decodeBitmap(source))
                        binding.llUploadPic.hide()
                    } else {
                        binding.ivDog.setImageURI(uri)
                        binding.llUploadPic.hide()
                    }
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri!!)
                binding.ivDog.setImageBitmap(ImageDecoder.decodeBitmap(source))
                binding.llUploadPic.hide()
            } else {
                binding.ivDog.setImageURI(uri)
                binding.llUploadPic.hide()
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDistressPetAPI() {
        myDogViewModel.getDistressPetData(
            MyApp.getSharedPref().userId,
            binding.etPetDescription.text.toString(),
            pinLatitude,
            pinLongitude,
            MultipartFile.prepareFilePart(this, "pet_image[]", uri),
            type,
            notificationType
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.responseMessage?.snack(
                            Color.BLACK,
                            binding.parent
                        )
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }


                        confirmDialog()
                        //finish()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SOS")
        builder.setMessage("Thank you for your contribution towards helping a pet/stray in need")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }

        /* builder.setNegativeButton(android.R.string.no) { dialog, which ->
             Toast.makeText(
                 applicationContext,
                 android.R.string.no, Toast.LENGTH_SHORT
             ).show()
         }

         builder.setNeutralButton("Maybe") { dialog, which ->
             Toast.makeText(
                 applicationContext,
                 "Maybe", Toast.LENGTH_SHORT
             ).show()
         }*/
        builder.show()
    }

}
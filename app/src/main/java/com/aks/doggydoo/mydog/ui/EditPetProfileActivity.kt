package com.aks.doggydoo.mydog.ui

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
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityPetEditBinding
import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.aks.doggydoo.mydog.viewmodel.MyDogViewModel
import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPetProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetEditBinding
    private val REQUEST_PERMISSION = 100
    var petId: String = ""
    var petVaccinate: String = ""
    private lateinit var myDogViewModel: MyDogViewModel
    private var uri: Uri? = null
    private var ageYear: String = ""
    private var ageMonth: String = ""
    private var weightkg: String = ""
    private var weightGm: String = ""
    var selectedGender: String = ""
    var selectedGenderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callGetDogDescriptionAPI()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
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

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        petId = intent.getStringExtra("petId").toString()
        binding.llAge.visibility = View.GONE
        binding.llBreed.visibility = View.GONE
        binding.llGender.visibility = View.GONE
        binding.llWeight.visibility = View.GONE

        binding.tvAge.setOnClickListener {
            binding.llAge.visibility = View.VISIBLE
            binding.fromNumber.maxValue = 20
            binding.fromNumber.minValue = 0
            binding.toNumber.maxValue = 12
            binding.toNumber.minValue = 0

        }

        binding.tvConfirmAge.setOnClickListener {
            binding.llAge.visibility = View.GONE
            binding.tvAge.text =
                binding.fromNumber.value.toString() + " " + "Year" + " " + binding.toNumber.value.toString() + " " + "Month"
        }


        binding.tvweight.setOnClickListener {
            binding.llWeight.visibility = View.VISIBLE
            binding.fromNumberw.maxValue = 999
            binding.fromNumberw.minValue = 1
            binding.toNumberw.maxValue = 100
            binding.toNumberw.minValue = 1
        }

        binding.tvConfirmWeight.setOnClickListener {
            binding.llWeight.visibility = View.GONE
            binding.tvweight.text =
                    //binding.fromNumberw.value.toString() + " " + "gm" + " " + binding.toNumberw.value.toString() + " " + "kg"
                binding.toNumberw.value.toString() + " " + "kg" + " " + binding.fromNumberw.value.toString() + " " + "gm"
        }

        binding.tvProfilePic.setOnClickListener {
            ChooseOption()
        }

        binding.rbYes.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.rbYes.isChecked = true
                binding.rbNo.isChecked = false
                petVaccinate = "yes"
            }
        }

        binding.rbNo.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.rbYes.isChecked = false
                binding.rbNo.isChecked = true
                petVaccinate = "no"
            }
        }

        binding.tvgender.setOnClickListener {
            binding.llGender.visibility = View.VISIBLE
        }

        binding.rlFemale.setOnClickListener {
            binding.ivTickFemale.visibility = View.VISIBLE
            binding.ivTickMale.visibility = View.GONE
            //  binding.ivTickOther.visibility = View.GONE
            selectedGender = "Female"
            selectedGenderId = "2"
        }

        binding.rlMale.setOnClickListener {
            binding.ivTickMale.visibility = View.VISIBLE
            binding.ivTickFemale.visibility = View.GONE
            //  binding.ivTickOther.visibility = View.GONE
            selectedGender = "Male"
            selectedGenderId = "1"
        }



        binding.tvConfirm.setOnClickListener {
            binding.tvgender.text = selectedGender
            binding.llGender.visibility = View.GONE
        }


        binding.tvSubmit.setOnClickListener {
            try {
                val currentAge = binding.tvAge.text
                val age = currentAge.split(" ").toTypedArray()
                ageYear = age[0]
                ageMonth = age[2]

                val currentWeight = binding.tvweight.text
                val weight = currentWeight.split(" ").toTypedArray()
                weightkg = weight[0]
                weightGm = weight[2]

            } catch (e: Exception) {

            }

            updatePetAPI()

        }

        binding.backButton.setOnClickListener {
            finish()
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

    private fun setDataInUI(data: PetDescriptionResponse) {
        if (data.petImage.size > 0)
            binding.petImage.loadImageFromString(
                this,
                ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
            )

        binding.etName.setText(data.petdetail.pet_name)
        binding.tvAge.text = "${data.petdetail.pet_age} Year ${data.petdetail.pet_age_month} Month"
        binding.tvBreed.text = data.petdetail.breed
        binding.tvgender.text = data.petdetail.pet_gender
        binding.tvweight.text =
            "${data.petdetail.pet_weight} kg ${data.petdetail.pet_weight_gm} gm"
        binding.etdescription.setText(data.petdetail.pet_description)
        if (data.petdetail.is_pet_vaccinated == "yes") {
            binding.rbYes.isChecked = true
        } else {
            binding.rbNo.isChecked = true
        }
        binding.etMedicalCondition.setText(data.petdetail.pet_medical_conditions)

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
                .compress(500)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    300,
                    300
                )    //Final image resolution will be less than 1080 x 1080(Optional)
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
                        binding.petImage.setImageBitmap(ImageDecoder.decodeBitmap(source))
                    } else {
                        binding.petImage.setImageURI(uri)
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
                binding.petImage.setImageBitmap(ImageDecoder.decodeBitmap(source))
            } else {
                binding.petImage.setImageURI(uri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePetAPI() {
        myDogViewModel.getEditPetData(
            petId,
            binding.etName.text.toString(),
            ageYear,
            ageMonth,
            "",
            binding.tvgender.text.toString(),
            weightkg,
            weightGm,
            binding.etdescription.text.toString(),
            petVaccinate,
            binding.etMedicalCondition.text.toString().trim(),
            MultipartFile.prepareFilePart(this, "pet_image[]", uri)
        )
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

                        startActivity(
                            Intent(
                                this,
                                MyDog::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}
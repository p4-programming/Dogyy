package com.bnb.doggydoo.myprofile.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityEditProfileBinding
import com.bnb.doggydoo.homemodule.ui.HomeActivity
import com.bnb.doggydoo.myprofile.datasource.model.profile.EditProfileResponse
import com.bnb.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.bnb.doggydoo.myprofile.viewmodel.MyProfileViewModel
import com.bnb.doggydoo.newsfeed.util.RecyclerTouchListener
import com.bnb.doggydoo.onboarding.adapter.UserIdSuggestionAdapter
import com.bnb.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    var selectedGender: String = ""
    var selectedGenderId: String = ""
    var selectedVisibility: String = ""
    private var date: OnDateSetListener? = null
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private lateinit var myProfileViewModel: MyProfileViewModel
    private lateinit var adapter: UserIdSuggestionAdapter
    private var userName = ArrayList<String>()
    var finalUserName: String = ""
    private val REQUEST_PERMISSION = 100
    private var uri: Uri? = null
    private var locationManager: LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var finallati: Double? = null
    var finallongi: Double? = null
    val SPLASH_TIME_OUT = 3000

    private val requestArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getMyProfileInfoAPI()
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

    private fun getLocationDetail() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                binding.progressBarloc.visibility = View.GONE
                finallati = location.latitude
                finallongi = location.longitude
                binding.tvLatitude.text = finallati.toString()
                binding.tvLongitude.text = finallongi.toString()
            }
        }

    }

    private fun getInit() {
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.llGender.visibility = View.GONE
        binding.tvGender.setOnClickListener {
            binding.llGender.visibility = View.VISIBLE
        }

        binding.rlFemale.setOnClickListener {
            binding.ivTickFemale.visibility = View.VISIBLE
            binding.ivTickMale.visibility = View.GONE
            binding.ivTickOther.visibility = View.GONE
            selectedGender = "Female"
            selectedGenderId = "2"
        }

        binding.rlMale.setOnClickListener {
            binding.ivTickMale.visibility = View.VISIBLE
            binding.ivTickFemale.visibility = View.GONE
            binding.ivTickOther.visibility = View.GONE
            selectedGender = "Male"
            selectedGenderId = "1"
        }

        binding.rlOther.setOnClickListener {
            binding.ivTickOther.visibility = View.VISIBLE
            binding.ivTickFemale.visibility = View.GONE
            binding.ivTickMale.visibility = View.GONE
            selectedGender = "Others"
            selectedGenderId = "3"
        }

        binding.tvConfirm.setOnClickListener {
            binding.tvGender.text = selectedGender
            binding.llGender.visibility = View.GONE
        }

        binding.llVisibility.visibility = View.GONE
        binding.tvProfileVisibility.setOnClickListener {
            binding.llVisibility.visibility = View.VISIBLE
        }

        binding.rlPublic.setOnClickListener {
            binding.ivTickPublic.visibility = View.VISIBLE
            binding.ivTickPrivate.visibility = View.GONE
            selectedVisibility = "public"
        }

        binding.rlPrivate.setOnClickListener {
            binding.ivTickPublic.visibility = View.GONE
            binding.ivTickPrivate.visibility = View.VISIBLE
            selectedVisibility = "private"
        }

        binding.tvConfirm1.setOnClickListener {
            binding.tvProfileVisibility.text = selectedVisibility
            binding.llVisibility.visibility = View.GONE
        }

        if (!binding.etPhone.text.isEmpty()) {
            binding.etPhone.isEnabled = false
        }

        val c = Calendar.getInstance().time
        val myCalendar = Calendar.getInstance(TimeZone.getDefault())
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = df.format(c)
        binding.tvDOB.setText(formattedDate)

        binding.tvDOB.setOnClickListener {
            date =
                OnDateSetListener { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    binding.tvDOB.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }
            CommonMethod.showDateDialog(this@EditProfileActivity, date, myCalendar)
        }

        adapter = UserIdSuggestionAdapter(this)
        binding.rvUserId.adapter = adapter
        binding.rvUserId.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.rvUserId,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        finalUserName = userName.get(position)
                        binding.etUserName.setText("@" + finalUserName)
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        binding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length > 3) {
                        val text: String = s.toString()
                        getUserNameData(text)
                    }
                }
            }
        })

        binding.tvProfilePic.setOnClickListener {
            ChooseOption()
        }

        binding.tvSubmit.setOnClickListener {
            editUserProfileAPI()
        }

        finalUserName = MyApp.getSharedPref().userIdName
        System.out.println("username>>" + finalUserName)

        binding.progressBarloc.visibility = View.GONE
        binding.ivUpdateLocation.setOnClickListener {
            binding.progressBarloc.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                getLocationDetail()
            }, SPLASH_TIME_OUT.toLong())

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
                .crop()
                .compress(500)
                .maxResultSize(
                    300,
                    300
                )
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
                        binding.userImage.setImageBitmap(ImageDecoder.decodeBitmap(source))
                    } else {
                        binding.userImage.setImageURI(uri)
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
                binding.userImage.setImageBitmap(ImageDecoder.decodeBitmap(source))
            } else {
                binding.userImage.setImageURI(uri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserNameData(text: String) {
        onBoardingViewModel.getUserName(text, MyApp.getSharedPref().userId)
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar1.show()
                        binding.tvAvailable.visibility = View.GONE
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }
                        //set adapter
                        if (it.data.userNameList.size > 0) {
                            binding.tvAvailable.visibility = View.GONE
                            binding.progressBar1.hide()
                            for (category in it.data.userNameList) {
                                userName.add(category.user_name)
                            }
                            adapter.submitList(it.data.userNameList)
                        } else {
                            binding.tvAvailable.visibility = View.VISIBLE
                            binding.progressBar1.hide()
                        }

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar1.hide()
                        it.message?.snack(Color.RED, binding.root)
                    }
                }
            })
    }


    private fun getMyProfileInfoAPI() {
        System.out.println("user id>>" + MyApp.getSharedPref().userId)
        myProfileViewModel.getMyProfileInfoData(MyApp.getSharedPref().userId,MyApp.getSharedPref().userId)
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }

                        setDataInUII(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUII(profileResponse: MyProfileResponse) {
        binding.etName.setText(profileResponse.userdetails[0].uname)
        binding.etUserName.setText(profileResponse.userdetails[0].username)
        binding.etPhone.setText(profileResponse.userdetails[0].mobile)
        binding.etEmail.setText(profileResponse.userdetails[0].email)
        binding.tvDOB.setText(profileResponse.userdetails[0].DOB)
        binding.tvGender.setText(profileResponse.userdetails[0].gender)
        binding.tvProfileVisibility.setText(profileResponse.userdetails[0].visibility)
        binding.etBio.setText(profileResponse.userdetails[0].description)
        binding.tvLatitude.text = profileResponse.userdetails[0].lattitute
        binding.tvLongitude.text = profileResponse.userdetails[0].longitute

        binding.userImage.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
        )


    }


    private fun editUserProfileAPI() {
        myProfileViewModel.getEditUserData(
            MyApp.getSharedPref().userId,
            binding.etName.text.toString(),
            "",
            binding.etBio.text.toString(),
            binding.etUserName.text.toString(),
            binding.tvDOB.text.toString(),
            binding.etEmail.text.toString(),
            selectedGenderId,
            selectedVisibility,
            binding.tvLatitude.text.trim().toString(),
            binding.tvLongitude.text.trim().toString(),
            MultipartFile.prepareFilePart(this, "user_profile", uri)

        ).observe(this, androidx.lifecycle.Observer {
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

    private fun saveData(data: EditProfileResponse?) {
        CommonMethod.showSnack(binding.root, "Profile updated successfully.")
        MyApp.getSharedPref().userId = data?.user_id
        MyApp.getSharedPref().userName = data?.uname
        MyApp.getSharedPref().userImage = data?.profile_pic
        MyApp.getSharedPref().userMobile = data?.mobile
        MyApp.getSharedPref().userIdName = data?.username
        MyApp.getSharedPref().userEmail = binding.etEmail.text.toString()

        startActivity(
            Intent(
                this,
                HomeActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}
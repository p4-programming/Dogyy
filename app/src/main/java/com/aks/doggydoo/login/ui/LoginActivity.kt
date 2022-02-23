package com.aks.doggydoo.login.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.LoginBinding
import com.aks.doggydoo.firebaseChat.User
import com.aks.doggydoo.homemodule.ui.HomeActivity
import com.aks.doggydoo.login.datasource.model.VerifyOtpResponse
import com.aks.doggydoo.login.viewmodel.LoginViewModel
import com.aks.doggydoo.onboarding.ui.OnBoardingActivity
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


private const val TAG = "loginTag"

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!
    var lati: String = ""
    var longi: String = ""
    var finallati: Double? = null
    var finallongi: Double? = null
    var firebaseToke: String = ""
    private var locationManager: LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var versionName: String = ""
    private var profilePic: String =""
    private  var userName: String =""

    //initialized view model here
    private lateinit var loginViewModel: LoginViewModel

    val TIME_OUT = 60

    private lateinit var currentUserPhone: TextView
    private lateinit var auth: FirebaseAuth
    private var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var verificationCode: String = ""
    private lateinit var mDbRef: DatabaseReference

    private val requestArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
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
                finallati = location.latitude
                finallongi = location.longitude
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@LoginActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            firebaseToke = task.result

            // Log and toast
            //  Toast.makeText(baseContext, firebaseToke, Toast.LENGTH_SHORT).show()
        })
    }

    private fun getInit() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        startFirebaseLogin()
        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )

        if (ContextCompat.checkSelfPermission(
                this@LoginActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LoginActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@LoginActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        binding.underlyingText.paintFlags =
            binding.underlyingText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.apply {
            Glide.with(this@LoginActivity).asGif().load(R.raw.login).centerCrop().into(imageview)
            button.setOnClickListener {
                if (button.text == getString(R.string.sendCode)) {
                    if (binding.phoneNumberEt.text.toString().length < 10) {
                        "Enter Valid Mobile Number".snack(
                            R.color.dogditting_bg_color,
                            binding.parent
                        )
                        return@setOnClickListener
                    }
                    if (isGpsEnabled()) {
                        getLocationDetail()
                        binding.progressBar.visibility = View.VISIBLE
                        val phoneNo = "+91" + phoneNumberEt.text.toString().trim()
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNo)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(this@LoginActivity)
                            .setCallbacks(mCallback!!)
                            .build()
                        PhoneAuthProvider.verifyPhoneNumber(options)

                    } else {
                        CommonMethod.showSnack(binding.parent, "Please enable GPS.")
                    }


                } else {
                    if (binding.otpEt.text!!.isNotEmpty()) {
                        val credential =
                            PhoneAuthProvider.getCredential(
                                verificationCode,
                                binding.otpEt.text.toString()
                            )
                        SigninWithPhone(credential)
                    } else {
                        if (binding.otpEt.text.toString().length > 6) {
                            "Enter correct OTP".snack(
                                R.color.dogditting_bg_color,
                                binding.parent
                            )
                            return@setOnClickListener
                        }
                    }

                }
            }

            binding.underlyingText.visibility = View.GONE
            underlyingText.setOnClickListener {
                if (binding.underlyingText.text == getString(R.string.resendOtp)) {
                    binding.otpLayout.visibility = View.GONE
                    binding.underlyingText.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    val phoneNo = "+91" + binding.phoneNumberEt.text.toString().trim()
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this@LoginActivity)
                        .setCallbacks(mCallback!!)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                } else {
                    startActivity(
                        Intent(this@LoginActivity, OnBoardingActivity::class.java)
                            .putExtra("from", "login")
                    )
                }
            }
        }

        getToken()
    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callOtpVerificationAPI()
                } else {
                    Toast.makeText(this@LoginActivity, "Incorrect OTP", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun startFirebaseLogin() {
        auth = Firebase.auth
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@LoginActivity, "verification completed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginActivity, "Unable to verify the number.", Toast.LENGTH_LONG).show()
                binding.progressBar.hide()
                binding.underlyingText.show()
                binding.underlyingText.text = getString(R.string.resendOtp)
                binding.button.text = getString(R.string.sendCode)
                auth.signOut()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s
                binding.progressBar.hide()
                binding.otpLayout.show()
                binding.button.text = getString(R.string.verify)
                binding.underlyingText.show()
                binding.underlyingText.text = getString(R.string.resendOtp)
            }
        }
    }


    private fun callOtpVerificationAPI() {
        lati = finallati.toString()
        longi = finallongi.toString()

        try {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
            versionName = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        loginViewModel.getOtpVerificationData(
            binding.phoneNumberEt.text.toString().trim(),
            binding.otpEt.text.toString(),
            MyApp.getDeviceId(),
            firebaseToke,
            "android",
            versionName,
            MyApp.getIpAddress(),
            lati,
            longi,
            auth.currentUser?.uid.toString()
        )
            .observe(this, Observer {
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
                        saveDate(it.data)

                        if (it.data.is_register == "no") {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    OnBoardingActivity::class.java
                                ).putExtra("from", "login")
                            )
                        } else
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    HomeActivity::class.java
                                )
                            )

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        /*Shows snack bar on error*/
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun saveDate(data: VerifyOtpResponse) {
        MyApp.getSharedPref().userId = data.user_id
        MyApp.getSharedPref().userName = data.uname
        MyApp.getSharedPref().userImage = data.profile_pic
        MyApp.getSharedPref().userMobile = data.mobile
        MyApp.getSharedPref().userRegistered = data.is_register
        MyApp.getSharedPref().userEmail = data.userData[0].email

        if (data.profile_pic.isEmpty()){
            profilePic = ""
        }else{
            profilePic = data.profile_pic
        }


        if (data.uname.isEmpty()){
            userName = "NA"
        }else{
            userName = data.uname
        }

        addUserToDatabase(data.mobile,profilePic,userName,data.user_id,auth.currentUser?.uid!!)
    }

    private fun addUserToDatabase(mobile: String, profilePic: String, uname: String, userId: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(mobile,profilePic,uname,userId,uid,"0",firebaseToke))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

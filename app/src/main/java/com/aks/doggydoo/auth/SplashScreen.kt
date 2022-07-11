package com.aks.doggydoo.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.aks.doggydoo.R
import com.aks.doggydoo.homemodule.ui.HomeActivity
import com.aks.doggydoo.login.ui.LoginActivity
import com.aks.doggydoo.utils.MyApp
import com.google.android.gms.location.*
import com.google.firebase.FirebaseApp

class SplashScreen : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var finallati: String = ""
    var finallongi: String = ""

    private val requestArray = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    fun Activity.makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }
    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()
        super.onCreate(savedInstanceState)
       // hideSystemBars()
        setContentView(R.layout.activity_splash_screen)
        FirebaseApp.initializeApp(this)
        getInit()

//        val videoview = findViewById<View>(R.id.videoView) as VideoView
//        videoview!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.introsceneh))
//        videoview.start()

        Handler(Looper.getMainLooper()).postDelayed({
            if (MyApp.getSharedPref().userRegistered == "no") {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else {
                if (MyApp.getSharedPref().userId.isEmpty()) {
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this@SplashScreen,
                            HomeActivity::class.java
                        )
                    )
                }

            }

        }, 5000)
    }

    private fun getInit() {
        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        if (locationManager != null){
            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocationDetail()
            }
        } else {
            Toast.makeText(this, "Please enable location service.", Toast.LENGTH_LONG).show()
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
                finallati = location.latitude.toString()
                finallongi = location.longitude.toString()
            }
        }
        if (!finallati.isEmpty() && !finallongi.isEmpty()) {
            MyApp.getSharedPref().userLat = finallati
            MyApp.getSharedPref().userLong = finallongi
        }

    }


}
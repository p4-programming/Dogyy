package com.bnb.doggydoo.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    private var locationManager: LocationManager? = null

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
        deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        prefManager = PrefManager(applicationContext)

        val wm: WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        ipAddress = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)


        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }


    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lat = "${location.latitude}"
            lng = "${location.longitude}"
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    companion object {
        private var instance: MyApp? = null
        private lateinit var deviceId: String
        private lateinit var ipAddress: String
        private var lat: String? = null
        private var lng: String? = null
        private var type:String? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var prefManager: PrefManager

        fun getNetworkStatus(): Boolean {
            return instance!!.isConnectionOn()
        }

        fun getIpAddress() = ipAddress
        fun getSharedPref() = prefManager
        fun getLatitude() = lat

        fun getLongitude() = lng
        fun getType() = type

        fun getDeviceId() = deviceId
    }


    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }
}
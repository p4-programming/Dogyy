package com.aks.doggydoo.login.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.login.datasource.repository.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(var loginRepo: LoginRepo) : ViewModel() {

    fun getSendOtpData(mobile: String,fcm_key: String, deviceType: String) =
        loginRepo.sendOtpLiveData(mobile,fcm_key,deviceType)

    fun getOtpVerificationData(
        mobile: String,
        otp: String,
        device_id: String,
        fcm_key: String,
        deviceType: String,
        android_version:String,
        ipaddress:String,
        latitude: String,
        longitude: String,
        userUid: String
    ) =
        loginRepo.sendVerifyOtpLiveData(
            mobile = mobile,
            otp = otp,
            device_id = device_id,
            fcm_key = fcm_key,
            deviceType = deviceType,
            android_version=android_version,
            ipaddress  = ipaddress,
            latitude = latitude,
            longitude = longitude,
            userUid = userUid
        )
}
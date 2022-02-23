package com.aks.doggydoo.login.datasource.repository

import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepo @Inject constructor(
    private var remoteLoginDataSource: RemoteLoginDataSource
) {
    fun sendOtpLiveData(mobile: String,fcm_key: String, deviceType: String) = resultLiveData(
        networkCall = { remoteLoginDataSource.fetchSendOtpResponse(mobile,fcm_key,deviceType) }
    )

    fun sendVerifyOtpLiveData(
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
    ) = resultLiveData(
        networkCall = {
            remoteLoginDataSource.fetchOtpVerificationResponse(
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
    )
}
package com.bnb.doggydoo.login.datasource.repository

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteLoginDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch SendOtp Response from network
     * */
    suspend fun fetchSendOtpResponse(mobile: String, fcm_key: String, deviceType: String) =
        getResult {
            apiFactory.createService(LoginApiService::class.java)
                .sendOtp(mobile, fcm_key, deviceType)
        }

    /**
     * fetch OtpVerification Response from network
     * */
    suspend fun fetchOtpVerificationResponse(
        mobile: String,
        otp: String,
        device_id: String,
        fcm_key: String,
        deviceType: String,
        android_version: String,
        ipaddress: String,
        latitude: String,
        longitude: String,
        userUid: String
    ) =
        getResult {
            apiFactory.createService(LoginApiService::class.java)
                .userOtpVerification(
                    mobile = mobile,
                    otp = otp,
                    device_id = device_id,
                    fcm_key = fcm_key,
                    deviceType = deviceType,
                    android_version = android_version,
                    ipaddress = ipaddress,
                    latitude = latitude,
                    longitude = longitude,
                    userUid = userUid
                )
        }
}
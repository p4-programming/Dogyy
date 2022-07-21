package com.bnb.doggydoo.login.datasource.repository

import com.bnb.doggydoo.login.datasource.model.SendOtpResponse
import com.bnb.doggydoo.login.datasource.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApiService {
    /**
     * First API to be run is SEND_OTP
     */
    @FormUrlEncoded
    @POST("AuthApi/registration")
    suspend fun sendOtp(
        @Field("mobile") mobile: String,
        @Field("fcm_key") fcm_key: String,
        @Field("deviceType") deviceType: String
    ): Response<SendOtpResponse>

    /**
     * Second API to be run is USER_OTP_VERIFICATION
     */
    @FormUrlEncoded
    @POST("AuthApi/userOtpVerification")
    suspend fun userOtpVerification(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String,
        @Field("device_id") device_id: String,
        @Field("fcm_key") fcm_key: String,
        @Field("deviceType") deviceType: String,
        @Field("android_version") android_version: String,
        @Field("ipaddress") ipaddress: String,
        @Field("latitude") latitude: String?,
        @Field("longitude") longitude: String?,
        @Field("userUid") userUid: String?
    ): Response<VerifyOtpResponse>

}
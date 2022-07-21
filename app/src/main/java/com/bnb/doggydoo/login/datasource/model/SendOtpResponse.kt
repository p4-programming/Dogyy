package com.bnb.doggydoo.login.datasource.model

import com.google.gson.annotations.SerializedName

class SendOtpResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("otp")
    var otp: String,
    @SerializedName("mobile")
    var mobile: String
)
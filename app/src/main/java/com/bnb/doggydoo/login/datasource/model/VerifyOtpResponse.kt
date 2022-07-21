package com.bnb.doggydoo.login.datasource.model

import com.google.gson.annotations.SerializedName

class VerifyOtpResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("is_register")
    var is_register: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("profile_pic")
    var profile_pic: String,
    @SerializedName("user_all_data")
    var userData: List<AllUserData>
)

package com.bnb.doggydoo.onboarding.datasource.model.user

import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("profile_pic")
    var profile_pic: String,
    @SerializedName("hash_token")
    var hash_token: String,
    @SerializedName("username")
    var username: String,
)

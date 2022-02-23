package com.aks.doggydoo.myprofile.datasource.model.profile

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("profile_pic")
    var profile_pic: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("hash_token")
    var hash_token: String
)

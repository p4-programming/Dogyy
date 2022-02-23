package com.aks.doggydoo.myprofile.datasource.model

import com.google.gson.annotations.SerializedName

data class UserImageUploadResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: String,
    @SerializedName("profile_image")
    var profile_image: String,

)
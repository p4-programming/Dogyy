package com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("id") val id: String,
    @SerializedName("km") val km: String,
    @SerializedName("uname") val uname: String,
    @SerializedName("address") val address: String,
    @SerializedName("profile_pic") val profile_pic: String,
    @SerializedName("description") val description: String,
    @SerializedName("userUid") val userUid: String
)

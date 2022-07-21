package com.bnb.doggydoo.login.datasource.model

import com.google.gson.annotations.SerializedName

data class AllUserData(
    @SerializedName("visibility") val visibility: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("DOB") val DOB: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("gender") val gender: String
)

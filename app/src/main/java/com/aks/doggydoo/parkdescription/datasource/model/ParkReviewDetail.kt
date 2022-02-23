package com.aks.doggydoo.parkdescription.datasource.model

import com.google.gson.annotations.SerializedName

data class ParkReviewDetail(
    @SerializedName("rate")
    var rate: String,
    @SerializedName("review")
    var review: String,
    @SerializedName("create_date")
    var create_date: String,
    @SerializedName("uname")
    var userName: String,
    @SerializedName("profile_pic")
    var profilePic: String
)
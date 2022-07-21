package com.bnb.doggydoo.callawet.datasource.model.home

import com.google.gson.annotations.SerializedName

data class NearByHospitalList(
    @SerializedName("id")
    var id: String,
    @SerializedName("km")
    var km: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("place_image")
    var place_image: String
)

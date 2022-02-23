package com.aks.doggydoo.callawet.datasource.model.nearbyhos

import com.google.gson.annotations.SerializedName

data class ClinicDetailDetailList(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("km")
    var km: String,
    @SerializedName("place_description")
    var place_description: String,
    @SerializedName("place_image")
    var place_image: List<String>

)

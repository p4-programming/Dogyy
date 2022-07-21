package com.bnb.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class ShelterList(
    @SerializedName("shelter_id")
    var shelter_id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("photo")
    var pic: String
)

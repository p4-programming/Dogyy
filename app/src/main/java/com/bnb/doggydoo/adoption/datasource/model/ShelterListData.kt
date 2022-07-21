package com.bnb.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class ShelterListData(
    @SerializedName("id")
    var id: String,
    @SerializedName("Name")
    var Name: String,
    @SerializedName("Breeds")
    var Breeds: String,
    @SerializedName("photo")
    var photo: String,
    @SerializedName("address")
    var address: String,

)

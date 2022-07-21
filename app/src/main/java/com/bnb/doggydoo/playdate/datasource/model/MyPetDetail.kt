package com.bnb.doggydoo.playdate.datasource.model

import com.google.gson.annotations.SerializedName

data class MyPetDetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("pet_name")
    var pet_name: String
)

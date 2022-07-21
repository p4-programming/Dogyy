package com.bnb.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class ViewAllPetList(
    @SerializedName("id")
    var id: String,
    @SerializedName("pet_name")
    var pet_name: String
)

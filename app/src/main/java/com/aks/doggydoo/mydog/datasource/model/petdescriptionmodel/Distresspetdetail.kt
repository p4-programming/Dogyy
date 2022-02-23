package com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName

data class Distresspetdetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("pet_description")
    var pet_description: String,
    @SerializedName("lattitute")
    var lattitute: String,
    @SerializedName("longitute")
    var longitute: String,
    @SerializedName("pet_image")
    var petImage: List<String>,
)

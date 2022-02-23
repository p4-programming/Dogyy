package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class ParkPlayDate(
    @SerializedName("id")
    var id: String,
    @SerializedName("play_date")
    var play_date: String,
    @SerializedName("play_time")
    var play_time: String,
    @SerializedName("pet_image")
    var pet_image: String,
    @SerializedName("pet_name")
    var pet_name: String,
    @SerializedName("petid")
    var petid: String,


)

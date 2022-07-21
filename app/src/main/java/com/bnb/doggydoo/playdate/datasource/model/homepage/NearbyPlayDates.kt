package com.bnb.doggydoo.playdate.datasource.model.homepage

import com.google.gson.annotations.SerializedName

data class NearbyPlayDates(
    @SerializedName("id")
    var id: String,
    @SerializedName("pet_id")
    var pet_id: String,
    @SerializedName("pet_name")
    var pet_name: String,
    @SerializedName("breed")
    var breed: String,
    @SerializedName("pet_age")
    var pet_age: String,
    @SerializedName("pet_age_month")
    var pet_age_month: String,
    @SerializedName("pet_age_type")
    var pet_age_type: String,
    @SerializedName("pet_image")
    var pet_image: String
)

package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class UserPetPlayList(
    @SerializedName("id")
    var id: String,
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

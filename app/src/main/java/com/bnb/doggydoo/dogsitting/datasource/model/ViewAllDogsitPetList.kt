package com.bnb.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class ViewAllDogsitPetList(
    @SerializedName("id")
    var id: String,
    @SerializedName("pet_id")
    var pet_id: String,
    @SerializedName("km")
    var km: String,
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
    var pet_image: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("profile_pic")
    var profile_pic: String,
    @SerializedName("lattitue")
    var lattitue: String,
    @SerializedName("longitute")
    var longitute: String
)

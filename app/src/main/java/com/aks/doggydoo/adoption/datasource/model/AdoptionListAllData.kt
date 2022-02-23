package com.aks.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class AdoptionListAllData(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("age")
    var age: String,
    @SerializedName("pet_age_month")
    var pet_age_month: String,
    @SerializedName("pet_age_type")
    var pet_age_type: String,
    @SerializedName("pic")
    var pic: String,
    @SerializedName("breed")
    var breed: String,
    @SerializedName("pet_gender")
    var pet_gender: String,
    @SerializedName("lattitue")
    var lattitue: String,
    @SerializedName("longitute")
    var longitute: String,
)

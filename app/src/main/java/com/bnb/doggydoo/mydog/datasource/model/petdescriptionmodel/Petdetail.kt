package com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName


data class Petdetail(
    @SerializedName("id") val id: String,
    @SerializedName("pet_name") val pet_name: String,
    @SerializedName("pet_age") val pet_age: String,
    @SerializedName("pet_age_month") val pet_age_month: String,
    @SerializedName("pet_age_type") val pet_age_type: String,
    @SerializedName("pet_gender") val pet_gender: String,
    @SerializedName("pet_weight") val pet_weight: String,
    @SerializedName("pet_weight_gm") val pet_weight_gm: String,
    @SerializedName("pet_weight_type") val pet_weight_type: String,
    @SerializedName("pet_description") val pet_description: String,
    @SerializedName("living") val living: String,
    @SerializedName("pet_medical_conditions") val pet_medical_conditions: String,
    @SerializedName("is_pet_vaccinated") val is_pet_vaccinated: String,
    @SerializedName("pet_image") val pet_image: String,
    @SerializedName("fostering") val fostering: String,
    @SerializedName("mating") val mating: String,
    @SerializedName("doggysit") val doggysit: String,
    @SerializedName("adoption") val adoption: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("uname") val uname: String,
    @SerializedName("address") val address: String,
    @SerializedName("km") val km: String,
    @SerializedName("profile_pic") val profile_pic: String,
    @SerializedName("userUid") val userUid: String,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_date") val end_date: String,
    @SerializedName("end_time") val end_time: String
)
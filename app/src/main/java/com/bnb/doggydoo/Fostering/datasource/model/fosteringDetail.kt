package com.bnb.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class fosteringDetail(
    @SerializedName("pet_id")
    var pet_id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("age")
    var age: String,
    @SerializedName("age_type")
    var age_type: String,
    @SerializedName("breed")
    var breed: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("km")
    var km: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("user_photo")
    var user_photo: String,
    @SerializedName("pic")
    var picList: List<String>,
    @SerializedName("user_name")
    var user_name: String,
    @SerializedName("userUid")
    var userUid: String
)

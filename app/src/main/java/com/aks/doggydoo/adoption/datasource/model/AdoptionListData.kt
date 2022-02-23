package com.aks.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class AdoptionListData(
    @SerializedName("pet_id")
    var pet_id: String,
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
    @SerializedName("description")
    var description: String
)
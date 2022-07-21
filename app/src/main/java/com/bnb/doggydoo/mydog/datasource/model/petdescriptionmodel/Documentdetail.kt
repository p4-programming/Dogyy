package com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName


data class Documentdetail(
    @SerializedName("caption") val caption: String,
    @SerializedName("create_date") val create_date: String,
    @SerializedName("document") val document: String,
    @SerializedName("id") val id: String,
    @SerializedName("pet_id") val pet_id: String

)
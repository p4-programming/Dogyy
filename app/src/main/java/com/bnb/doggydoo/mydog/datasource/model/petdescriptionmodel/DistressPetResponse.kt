package com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName

data class DistressPetResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("Distresspetdetail")
    var distressPetdetail: Distresspetdetail
)

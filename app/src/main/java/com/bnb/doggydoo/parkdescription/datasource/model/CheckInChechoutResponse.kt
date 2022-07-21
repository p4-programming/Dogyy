package com.bnb.doggydoo.parkdescription.datasource.model

import com.google.gson.annotations.SerializedName

data class CheckInChechoutResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

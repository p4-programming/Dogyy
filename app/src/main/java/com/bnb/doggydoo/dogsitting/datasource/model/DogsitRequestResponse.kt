package com.bnb.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class DogsitRequestResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

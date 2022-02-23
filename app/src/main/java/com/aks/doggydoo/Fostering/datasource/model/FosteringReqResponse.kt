package com.aks.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class FosteringReqResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

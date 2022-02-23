package com.aks.doggydoo.callawet.datasource.model.rate

import com.google.gson.annotations.SerializedName

data class RateDoctorResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

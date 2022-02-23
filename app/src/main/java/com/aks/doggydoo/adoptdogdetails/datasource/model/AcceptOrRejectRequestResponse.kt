package com.aks.doggydoo.adoptdogdetails.datasource.model

import com.google.gson.annotations.SerializedName

data class AcceptOrRejectRequestResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)
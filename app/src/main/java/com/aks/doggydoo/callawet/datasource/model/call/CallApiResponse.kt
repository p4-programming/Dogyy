package com.aks.doggydoo.callawet.datasource.model.call

import com.google.gson.annotations.SerializedName

data class CallApiResponse(
    @SerializedName("callid")
    var callid: String,
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

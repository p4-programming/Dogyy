package com.bnb.doggydoo.callawet.datasource.model.callreason

import com.google.gson.annotations.SerializedName

data class CallReasonResponse(
    @SerializedName("ReasonDetail")
    var reasonDetailList: List<ReasonDetailList>,
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String)

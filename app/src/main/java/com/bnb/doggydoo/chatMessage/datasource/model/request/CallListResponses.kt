package com.bnb.doggydoo.chatMessage.datasource.model.request

import com.google.gson.annotations.SerializedName

data class CallListResponses(
    @SerializedName("responseCode")
    var responseCode: String,

    @SerializedName("responseData")
    var callList: List<CallsList>,


    @SerializedName("responseMessage")
    var responseMessage: String
)

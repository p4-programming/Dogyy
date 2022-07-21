package com.bnb.doggydoo.chatMessage.datasource.model.request

import com.google.gson.annotations.SerializedName

data class SentRequestResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("RequestSendlist")
    var sentRequest: List<ReceiveRequest>,
    @SerializedName("RequestReceivelist")
    var receiveRequest: List<ReceiveRequest>,
    @SerializedName("responseMessage")
    var responseMessage: String
)
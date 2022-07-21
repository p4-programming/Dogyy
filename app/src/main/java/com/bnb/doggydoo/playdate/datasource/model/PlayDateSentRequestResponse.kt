package com.bnb.doggydoo.playdate.datasource.model

import com.google.gson.annotations.SerializedName

data class PlayDateSentRequestResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("requestsend")
    var requestsend: String
)

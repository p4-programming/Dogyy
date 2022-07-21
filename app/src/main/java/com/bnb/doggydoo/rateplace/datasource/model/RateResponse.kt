package com.bnb.doggydoo.rateplace.datasource.model

import com.google.gson.annotations.SerializedName

class RateResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: Boolean
)
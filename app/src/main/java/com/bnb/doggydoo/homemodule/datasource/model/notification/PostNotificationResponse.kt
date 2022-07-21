package com.bnb.doggydoo.homemodule.datasource.model.notification

import com.google.gson.annotations.SerializedName

data class PostNotificationResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: String
)

package com.aks.doggydoo.mydog.datasource.model

import com.google.gson.annotations.SerializedName

data class PetReminderResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: String
)
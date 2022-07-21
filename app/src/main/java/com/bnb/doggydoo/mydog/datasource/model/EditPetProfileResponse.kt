package com.bnb.doggydoo.mydog.datasource.model

import com.google.gson.annotations.SerializedName

data class EditPetProfileResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: String
)

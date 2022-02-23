package com.aks.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class RateHeroResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

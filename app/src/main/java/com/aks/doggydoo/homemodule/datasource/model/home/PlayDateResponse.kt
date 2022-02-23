package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class PlayDateResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("parkplaydate")
    var parkPlayDate: List<ParkPlayDate>
)
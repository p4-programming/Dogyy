package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class MapResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("ParkDetail")
    var ParkDetailList: List<MapParkDetail>
)

package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("room_id")
    var room_id: String,
    @SerializedName("refrence_id")
    var refrence_id: String,
    @SerializedName("type")
    var type: String
)

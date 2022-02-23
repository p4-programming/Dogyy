package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class UserStatusResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

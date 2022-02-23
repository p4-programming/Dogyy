package com.aks.doggydoo.myprofile.datasource.model.profile

import com.google.gson.annotations.SerializedName

data class UpdateStatusResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

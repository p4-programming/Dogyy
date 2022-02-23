package com.aks.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class ShelterListResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("sheltetlist")
    var shelterlist: List<ShelterListData>
)

package com.aks.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class AdoptionListAllResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("adoptionlist")
    var adoptionlist: List<AdoptionListAllData>
)

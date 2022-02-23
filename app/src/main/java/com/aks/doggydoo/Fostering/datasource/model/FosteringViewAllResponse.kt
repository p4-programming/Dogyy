package com.aks.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class FosteringViewAllResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("viewallforest")
    var allPetlist: List<NearByFosterPetList>
)

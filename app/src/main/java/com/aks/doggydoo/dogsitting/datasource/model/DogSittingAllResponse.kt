package com.aks.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class DogSittingAllResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("viewall")
    var alldogsitpetlist: List<ViewAllDogsitPetList>
)

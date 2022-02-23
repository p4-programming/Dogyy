package com.aks.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class AllDogResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("mypetdetail")
    var alldoglist: List<ViewAllPetList>
)

package com.bnb.doggydoo.adoptdogdetails.datasource.model

import com.google.gson.annotations.SerializedName

class AdoptionDogDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("adoptionlist")
    var adoptionList: List<AdoptionDogList>
)
package com.bnb.doggydoo.onboarding.datasource.model.pet

import com.google.gson.annotations.SerializedName

class PetBreedResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("petbreeddetail")
    var petbreeddetails : List<PetBreedDetail>
)
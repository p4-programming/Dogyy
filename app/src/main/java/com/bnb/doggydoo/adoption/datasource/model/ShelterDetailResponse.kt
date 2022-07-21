package com.bnb.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class ShelterDetailResponse(

    @SerializedName("responseCode")
    var responseCode: String,

    @SerializedName("responseMessage")
    var responseMessage: String,

    @SerializedName("Singlesheltetlist")
    var shelterlist: List<Singlesheltetlist>

)
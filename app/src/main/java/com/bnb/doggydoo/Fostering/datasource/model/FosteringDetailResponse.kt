package com.bnb.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class FosteringDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("fostering")
    var fosteringDetaillist: fosteringDetail
)

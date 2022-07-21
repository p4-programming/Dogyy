package com.bnb.doggydoo.callawet.datasource.model.nearbydoc

import com.google.gson.annotations.SerializedName

data class NearByDoctorDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("VetDetail")
    var doctorDetailList: List<NearByDoctorDetailList>
)

package com.bnb.doggydoo.callawet.datasource.model.home

import com.google.gson.annotations.SerializedName

data class ViewAllDoctorResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("nearbydoctor")
    var nearByDoclist: List<NearByDocList>
)

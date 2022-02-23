package com.aks.doggydoo.callawet.datasource.model.home

import com.google.gson.annotations.SerializedName

data class ViewAllClinicResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("nearbyhospital")
    var nearByHosList: List<NearByHospitalList>
)

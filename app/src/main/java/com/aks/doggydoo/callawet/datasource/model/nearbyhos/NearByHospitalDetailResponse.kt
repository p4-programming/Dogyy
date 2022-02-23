package com.aks.doggydoo.callawet.datasource.model.nearbyhos

import com.aks.doggydoo.callawet.datasource.model.home.NearByDocList
import com.google.gson.annotations.SerializedName

data class NearByHospitalDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("ClinicDetail")
    var clinicDetailList: List<ClinicDetailDetailList>,
    @SerializedName("Doctorlist")
    var doctorlist: List<NearByDocList>

)

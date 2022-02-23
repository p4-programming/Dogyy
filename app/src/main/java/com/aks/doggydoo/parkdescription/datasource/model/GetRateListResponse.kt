package com.aks.doggydoo.parkdescription.datasource.model

import com.google.gson.annotations.SerializedName

data class GetRateListResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("parkrate")
    var parkRate: Double,
    @SerializedName("Park_review_list")
    var parkreviewdetail: List<ParkReviewDetail>,
    @SerializedName("parkdetail")
    var parkdetail: List<ParkDetail>
)
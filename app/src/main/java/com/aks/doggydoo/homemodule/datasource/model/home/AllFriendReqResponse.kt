package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class AllFriendReqResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("total_accepte_request")
    var total_accepte_request: String,
    @SerializedName("FreindsRequestSendlist")
    var allReqList: List<FriendReqList>
)

package com.bnb.doggydoo.playdate.datasource.model

import com.google.gson.annotations.SerializedName

data class AllPetResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("mypetdetail")
    var mypetdetail: List<MyPetDetail>
)

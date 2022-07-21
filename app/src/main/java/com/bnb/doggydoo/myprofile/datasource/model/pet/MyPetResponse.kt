package com.bnb.doggydoo.myprofile.datasource.model.pet

import com.google.gson.annotations.SerializedName

data class MyPetResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("mypetdetail")
    var mypetdetail: List<MyPetDetail>

)

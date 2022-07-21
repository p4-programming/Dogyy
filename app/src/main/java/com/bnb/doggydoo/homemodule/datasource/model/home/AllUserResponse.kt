package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class AllUserResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("alluserslist")
    var allUserList: List<AllUserList>
)

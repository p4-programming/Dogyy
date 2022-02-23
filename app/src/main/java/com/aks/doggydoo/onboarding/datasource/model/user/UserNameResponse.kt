package com.aks.doggydoo.onboarding.datasource.model.user

import com.google.gson.annotations.SerializedName

data class UserNameResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("username_available")
    var username_available: String,
    @SerializedName("username_suggest")
    var userNameList : List<UserNameList>
)

package com.bnb.doggydoo.onboarding.datasource.model.user

import com.google.gson.annotations.SerializedName

data class UserNameList(
    @SerializedName("user_name")
    var user_name: String
)

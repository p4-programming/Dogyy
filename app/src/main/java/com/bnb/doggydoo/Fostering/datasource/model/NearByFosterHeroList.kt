package com.bnb.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class NearByFosterHeroList(
    @SerializedName("id")
    var id: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("profile_pic")
    var profile_pic: String
)

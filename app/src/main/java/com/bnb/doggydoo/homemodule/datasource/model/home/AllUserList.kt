package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class AllUserList(
    @SerializedName("id")
    var id: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("lattitute")
    var lattitute: String,
    @SerializedName("longitute")
    var longitute: String,
    @SerializedName("profile_pic")
    var profile_pic: String,
    @SerializedName("mobile")
    var mobile: String,

)

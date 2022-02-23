package com.aks.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class DogSitHerosList(
    @SerializedName("id")
    var id: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("profile_pic")
    var profile_pic: String
)

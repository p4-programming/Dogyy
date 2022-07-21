package com.bnb.doggydoo.adoptdogdetails.datasource.model

import com.google.gson.annotations.SerializedName

class UserOfAdoptionDog (
    @SerializedName("name")
    var name: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("address")
    var address:String,
    @SerializedName("photo")
    var photo:String,
    @SerializedName("userUid")
    var userUid:String
)

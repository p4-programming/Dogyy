package com.bnb.doggydoo.training.datasource.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("createon")
    var createon: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("userphoto")
    var userphoto: String
)

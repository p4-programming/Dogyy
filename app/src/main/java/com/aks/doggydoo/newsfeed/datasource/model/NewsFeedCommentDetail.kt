package com.aks.doggydoo.newsfeed.datasource.model

import com.google.gson.annotations.SerializedName

data class NewsFeedCommentDetail(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("createon")
    var createon: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("userphoto")
    var userphoto: String,
    @SerializedName("user_id")
    var user_id: String
)

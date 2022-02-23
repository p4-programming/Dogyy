package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class BlogCommentList(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("createdate")
    var createdate: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("userphoto")
    var userphoto: String,
    @SerializedName("userid")
    var userid: String
)

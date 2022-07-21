package com.bnb.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class Articledetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("file")
    var file: String,
    @SerializedName("caption")
    var caption: String,
    @SerializedName("article")
    var article: String,
    @SerializedName("post_date")
    var post_date: String,
    @SerializedName("commentcount")
    var commentcount: String,
    @SerializedName("countlike")
    var countlike: String,
    @SerializedName("like")
    var like: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("profile_pic")
    var profile_pic: String
)

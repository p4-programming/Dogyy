package com.bnb.doggydoo.newsfeed.datasource.model

import com.google.gson.annotations.SerializedName

data class NewsFeedDetail(
    @SerializedName("userUid")
    var userUid: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("user_name")
    var user_name: String,
    @SerializedName("user_pic")
    var user_pic: String?,
    @SerializedName("id")
    var id: String,
    @SerializedName("news_type")
    var news_type: String,
    @SerializedName("filetype")
    var filetype: String?,
    @SerializedName("file")
    var file: String?,
    @SerializedName("caption")
    var caption: String?,
    @SerializedName("article")
    var article: String?,
    @SerializedName("post_date")
    var post_date: String?,
    @SerializedName("commentcount")
    var commentcount: String,
    @SerializedName("countlike")
    var countlike: String,
    @SerializedName("like")
    var like: String

)

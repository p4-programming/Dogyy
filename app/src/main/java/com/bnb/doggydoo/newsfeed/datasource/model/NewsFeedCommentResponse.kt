package com.bnb.doggydoo.newsfeed.datasource.model

import com.google.gson.annotations.SerializedName

data class NewsFeedCommentResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("newsfeedCommentdetail")
    var newsfeedcommentList: List<NewsFeedCommentDetail>
)

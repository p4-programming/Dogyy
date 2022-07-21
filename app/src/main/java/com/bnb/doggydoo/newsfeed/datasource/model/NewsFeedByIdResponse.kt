package com.bnb.doggydoo.newsfeed.datasource.model

import com.google.gson.annotations.SerializedName

data class NewsFeedByIdResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("newsfeedlist")
    var newsfeeddetail: List<NewsFeedDetail>
)
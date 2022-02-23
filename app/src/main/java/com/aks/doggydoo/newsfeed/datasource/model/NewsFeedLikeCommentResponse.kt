package com.aks.doggydoo.newsfeed.datasource.model

import com.google.gson.annotations.SerializedName

data class NewsFeedLikeCommentResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

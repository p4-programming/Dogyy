package com.bnb.doggydoo.articledescription.datasource.model

import com.google.gson.annotations.SerializedName

class NewsFeedCommentResponse (
    @SerializedName("responseCode")
    var responseCode:String,
    @SerializedName("responseMessage")
    var responseMessage:String,
    @SerializedName("newsfeedCommentdetail")
    var newsfeedCommentdetail:List<NewsfeedCommentDetail>

        )
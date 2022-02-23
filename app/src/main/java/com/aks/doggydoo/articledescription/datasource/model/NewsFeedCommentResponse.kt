package com.aks.doggydoo.articledescription.datasource.model

import com.aks.doggydoo.parkdescription.datasource.model.ParkReviewDetail
import com.google.gson.annotations.SerializedName

class NewsFeedCommentResponse (
    @SerializedName("responseCode")
    var responseCode:String,
    @SerializedName("responseMessage")
    var responseMessage:String,
    @SerializedName("newsfeedCommentdetail")
    var newsfeedCommentdetail:List<NewsfeedCommentDetail>

        )
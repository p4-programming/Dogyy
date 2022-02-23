package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class ArticleCommentListResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("blogcommentdata")
    var blogcommentList: List<BlogCommentList>
)

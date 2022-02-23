package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class SingleArticleResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("blogdata")
    var singleblogDataList: List<Articledetail>
)

package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class AllArticleResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("allblog")
    var allblogList: List<Articledetail>
)

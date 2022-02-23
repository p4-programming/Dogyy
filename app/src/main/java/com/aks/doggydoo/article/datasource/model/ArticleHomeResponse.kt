package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class ArticleHomeResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("trendingblog")
    var trendingblogList: List<Articledetail>,
    @SerializedName("friendsblog")
    var friendsblogList: List<Articledetail>,
    @SerializedName("userblog")
    var userblogList: List<Articledetail>
)

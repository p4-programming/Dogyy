package com.aks.doggydoo.article.datasource.model

import com.google.gson.annotations.SerializedName

data class InsertArticleResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("hash_token")
    var hash_token: String
)
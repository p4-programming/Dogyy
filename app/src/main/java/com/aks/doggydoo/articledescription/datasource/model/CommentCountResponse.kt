package com.aks.doggydoo.articledescription.datasource.model

import com.google.gson.annotations.SerializedName

data class CommentCountResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("commentcount")
    var commentcount: String
)
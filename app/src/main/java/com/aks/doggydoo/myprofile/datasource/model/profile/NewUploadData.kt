package com.aks.doggydoo.myprofile.datasource.model.profile

import com.google.gson.annotations.SerializedName

data class NewUploadData(
    @SerializedName("id") val id: String,
    @SerializedName("filetype") val filetype: String,
    @SerializedName("file") val file: String,
    @SerializedName("short_description") val short_description: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("article") val article: String,
    @SerializedName("createdate") val createdate: String,
    @SerializedName("commentcount") val commentcount: String,
    @SerializedName("like") val like: String
)
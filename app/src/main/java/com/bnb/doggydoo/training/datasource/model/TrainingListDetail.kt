package com.bnb.doggydoo.training.datasource.model

import com.google.gson.annotations.SerializedName

data class TrainingListDetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("filetype")
    var filetype: String,
    @SerializedName("file")
    var file: String,
    @SerializedName("caption")
    var caption: String,
  @SerializedName("createdate")
    var createdate: String,
  @SerializedName("commentcount")
    var commentcount: String,
)

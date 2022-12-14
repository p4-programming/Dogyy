package com.bnb.doggydoo.training.datasource.model

import com.google.gson.annotations.SerializedName

data class TrainingDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("trainingdetail")
    var trainingdetail: List<TrainingListDetail>,
    @SerializedName("comment")
    var comment: List<Comment>
)
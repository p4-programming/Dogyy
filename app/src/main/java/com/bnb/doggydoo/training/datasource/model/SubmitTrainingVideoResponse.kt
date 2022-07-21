package com.bnb.doggydoo.training.datasource.model

import com.google.gson.annotations.SerializedName

data class SubmitTrainingVideoResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String
)

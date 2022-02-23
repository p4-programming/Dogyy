package com.aks.doggydoo.training.datasource.model

import com.google.gson.annotations.SerializedName

data class TrainingListReposne(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("trainingdetail")
    var trainingdetail: List<TrainingListDetail>
)
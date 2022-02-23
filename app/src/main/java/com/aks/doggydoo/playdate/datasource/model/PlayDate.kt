package com.aks.doggydoo.playdate.datasource.model

import com.google.gson.annotations.SerializedName

class PlayDate(
    @SerializedName("request_id")
    var request_id: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("day")
    var day: String,
    @SerializedName("date_mode")
    var date_mode: String,
)

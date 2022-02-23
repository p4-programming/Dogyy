package com.aks.doggydoo.homemodule.datasource.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationStatusList(
    @SerializedName("id")
    var id: String,
    @SerializedName("market")
    var market: String,
    @SerializedName("request")
    var request: String,
    @SerializedName("message_call")
    var message_call: String,
    @SerializedName("newsfeed")
    var newsfeed: String,
    @SerializedName("tracking")
    var tracking: String
)
package com.bnb.doggydoo.homemodule.datasource.model.notification

import com.google.gson.annotations.SerializedName

data class GetNotificationStatusResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("notificationdetail")
    var notificationStatusList: List<NotificationStatusList>
)
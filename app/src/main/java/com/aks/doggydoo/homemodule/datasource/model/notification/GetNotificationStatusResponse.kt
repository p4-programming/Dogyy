package com.aks.doggydoo.homemodule.datasource.model.notification

import com.aks.doggydoo.adoption.datasource.model.AdoptionListData
import com.google.gson.annotations.SerializedName

data class GetNotificationStatusResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("notificationdetail")
    var notificationStatusList: List<NotificationStatusList>
)
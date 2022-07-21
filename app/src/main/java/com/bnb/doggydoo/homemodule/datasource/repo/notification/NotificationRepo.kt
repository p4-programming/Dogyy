package com.bnb.doggydoo.homemodule.datasource.repo.notification

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepo @Inject constructor(
    private var remoteNotificationDataSource: RemoteNotificationDataSource
) {
    fun getParkDescriptionLiveData(
        userId: String
    ) = resultLiveData(
        networkCall = { remoteNotificationDataSource.fetchNotificationStatusResponse(userId) }
    )

    fun getPostNotificationResponse(userId: String, type: String, status: String)= resultLiveData(
        networkCall = { remoteNotificationDataSource.fetchPostNotificationStatusResponse(userId,type,status) }
    )

}
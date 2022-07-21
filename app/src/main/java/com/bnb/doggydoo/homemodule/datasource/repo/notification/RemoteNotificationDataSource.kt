package com.bnb.doggydoo.homemodule.datasource.repo.notification

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteNotificationDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch notificationStatus Response from network
     * */
    suspend fun fetchNotificationStatusResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(NotificationApiService::class.java)
                .getUserNotificationStatus(
                    user_id = user_id
                )
        }

    suspend fun fetchPostNotificationStatusResponse(userId: String, type: String, status: String) =
        getResult {
            apiFactory.createService(NotificationApiService::class.java)
                .postUserNotificationStatus(
                    user_id = userId,
                    type, status
                )
        }
}
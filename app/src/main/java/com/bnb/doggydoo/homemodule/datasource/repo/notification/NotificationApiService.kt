package com.bnb.doggydoo.homemodule.datasource.repo.notification

import com.bnb.doggydoo.homemodule.datasource.model.notification.GetNotificationStatusResponse
import com.bnb.doggydoo.homemodule.datasource.model.notification.PostNotificationResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NotificationApiService {

    @FormUrlEncoded
    @POST("NotificationApi/getusernotificationpermission")
    suspend fun getUserNotificationStatus(
        @Field("user_id") user_id: String
    ): Response<GetNotificationStatusResponse>

    @FormUrlEncoded
    @POST("NotificationApi/updatenotifypermission")
    suspend fun postUserNotificationStatus(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
        @Field("status") status: String
    ): Response<PostNotificationResponse>

}
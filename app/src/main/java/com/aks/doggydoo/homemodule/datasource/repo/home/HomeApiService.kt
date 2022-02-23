package com.aks.doggydoo.homemodule.datasource.repo.home

import com.aks.doggydoo.homemodule.datasource.model.home.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HomeApiService {
    @FormUrlEncoded
    @POST("HomeApi/getpetplay")
    suspend fun getUpcomingPlayDate(
        @Field("user_id") user_id: String,
        @Field("latitude") lattitute: String,
        @Field("longitude") longitute: String
    ): Response<PlayDateResponse>


    @FormUrlEncoded
    @POST("AuthApi/homemap")
    suspend fun getMapLocation(
        @Field("user_id") user_id: String,
        @Field("latitude") lattitute: String,
        @Field("longitude") longitute: String,
        @Field("type") type: String
    ): Response<MapResponse>


    @FormUrlEncoded
    @POST("AuthApi/usercheckstatus")
    suspend fun getUserStatus(
        @Field("user_id") user_id: String
    ): Response<UserStatusResponse>

    @FormUrlEncoded
    @POST("HomeApi/userupcoming")
    suspend fun getUserUpcoming(
        @Field("user_id") user_id: String
    ): Response<UserUpcomingResponse>

    @FormUrlEncoded
    @POST("HomeApi/FriendsRequestSendlist")
    suspend fun getAllFriendReq(
        @Field("user_id") user_id: String
    ): Response<AllFriendReqResponse>

    @FormUrlEncoded
    @POST("HomeApi/FriendsRequestAction")
    suspend fun acceptRejectFriend(
        @Field("Request_id") request_id: String,
        @Field("request_status") request_status: String
    ): Response<AcceptRejectResponse>

    @FormUrlEncoded
    @POST("AuthApi/alluserslist")
    suspend fun getAllUserList(
        @Field("user_id") user_id: String
    ): Response<AllUserResponse>

    @FormUrlEncoded
    @POST("HomeApi/viewall_upcoming_reminder")
    suspend fun getAllReminder(
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Response<AllReminderResponse>

    @FormUrlEncoded
    @POST("HomeApi/createroomid")
    suspend fun getToken(
        @Field("request_id") request_id: String,
        @Field("type") type: String
    ): Response<TokenResponse>


    @FormUrlEncoded
    @POST("HomeApi/createtoken")
    suspend fun getCreatedToken(
        @Field("user_name") user_name: String,
        @Field("refrence_id") refrence_id: String,
        @Field("room_id") room_id: String,
        @Field("user_id") user_id: String,
        @Field("type") type: String,
        @Field("notify_id") notify_id: String
    ): Response<TokenGenerateResponse>
}




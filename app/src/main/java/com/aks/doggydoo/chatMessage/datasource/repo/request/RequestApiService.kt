package com.aks.doggydoo.chatMessage.datasource.repo.request

import com.aks.doggydoo.adoptdogdetails.datasource.model.AcceptOrRejectRequestResponse
import com.aks.doggydoo.chatMessage.datasource.model.request.CallListResponses
import com.aks.doggydoo.chatMessage.datasource.model.request.SentRequestResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RequestApiService {
    @FormUrlEncoded
    @POST("HomeApi/RequestSendlist")
    suspend fun sendList(
        @Field("user_id") user_id: String
    ): Response<SentRequestResponse>

    @FormUrlEncoded
    @POST("HomeApi/RequestRecivelist")
    suspend fun receiveList(
        @Field("user_id") user_id: String
    ): Response<SentRequestResponse>

    @FormUrlEncoded
    @POST("HomeApi/RequestAction")
    suspend fun acceptOrReject(
        @Field("request_id") adoption_request_id: String,
        @Field("request_type") request_type: String,
        @Field("request_status") request_status: String,
        @Field("user_id") user_id: String,
        @Field("pet_id") pet_id: String
    ): Response<AcceptOrRejectRequestResponse>


    @FormUrlEncoded
    @POST(" HomeApi/call_list")
    suspend fun callList(
        @Field("user_id") user_id: String
    ): Response<CallListResponses>


}

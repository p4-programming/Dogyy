package com.aks.doggydoo.chatMessage.datasource.repo.request

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteRequestDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch SentRequest Response from network
     * */
    suspend fun fetchSentRequestResponse(
        userId: String
    ) =
        getResult {
            apiFactory.createService(RequestApiService::class.java)
                .sendList(
                    userId
                )
        }

    /**
     * fetch receiveRequest Response from network
     * */
    suspend fun fetchReceiveRequestResponse(
        userId: String
    ) =
        getResult {
            apiFactory.createService(RequestApiService::class.java)
                .receiveList(
                    userId
                )
        }

    /**
     * fetch AcceptReject Response from network
     * */
    suspend fun fetchAcceptRejectResponse(
        request_id: String,
        requestType: String,
        request_status: String,
        user_id: String,
        pet_id: String
    ) =
        getResult {
            apiFactory.createService(RequestApiService::class.java)
                .acceptOrReject(
                    request_id, requestType, request_status,user_id,pet_id
                )
        }
}
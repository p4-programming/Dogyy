package com.bnb.doggydoo.chatMessage.datasource.repo.request

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepo @Inject
constructor(
    private var remoteRequestDataSource: RemoteRequestDataSource
) {
    fun sentRequestLiveData(
        userId: String
    ) = resultLiveData(
        networkCall = { remoteRequestDataSource.fetchSentRequestResponse(userId) }
    )

    fun receiveRequestLiveData(
        userId: String
    ) = resultLiveData(
        networkCall = { remoteRequestDataSource.fetchReceiveRequestResponse(userId) }
    )

    fun acceptOrRejectLiveData(
        request_id: String,
        requestType: String,
        request_status: String,
        user_id: String,
        pet_id: String
    ) = resultLiveData(
        networkCall = {
            remoteRequestDataSource.fetchAcceptRejectResponse(
                request_id,
                requestType,
                request_status,
                user_id,
                pet_id
            )
        }
    )

    fun callListLiveData(
        userId: String
    ) = resultLiveData(
        networkCall = { remoteRequestDataSource.fetchCallListResponse(userId) }
    )

}
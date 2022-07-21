package com.bnb.doggydoo.adoptdogdetails.datasource.repo

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteAdoptionDogDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch Adoption Dog's Detail Response from network
     * */
    suspend fun fetchAdoptionDogDetailResponse(
        adoptid: String,
        userid : String
    ) =
        getResult {
            apiFactory.createService(AdoptionDogDetailApiService::class.java)
                .getAdoptionDetail(
                    adoptid,userid
                )
        }

    /**
     * fetch Send Adoption Response from network
     * */
    suspend fun fetchSendAdoptionDogResponse(
        userId: String,
        receiveId: String,
        petId: String
    ) =
        getResult {
            apiFactory.createService(AdoptionDogDetailApiService::class.java)
                .sendAdoptionRequest(
                    userId,
                    receiveId,
                    petId
                )
        }

}
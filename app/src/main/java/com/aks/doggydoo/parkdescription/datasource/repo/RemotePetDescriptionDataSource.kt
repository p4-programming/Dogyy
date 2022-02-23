package com.aks.doggydoo.parkdescription.datasource.repo

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemotePetDescriptionDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch ParkDescription Response from network
     * */
    suspend fun fetchParkDescriptionResponse(
        park_id: String, user_id: String
    ) =
        getResult {
            apiFactory.createService(PetDescriptionApiService::class.java)
                .getParkDescription(
                    park_id,user_id
                )
        }


    suspend fun fetchCheckInResponse(
        type: String, park_id: String, userid:String
    ) =
        getResult {
            apiFactory.createService(PetDescriptionApiService::class.java)
                .checkinCheckout(
                    type, park_id, userid
                )
        }
}
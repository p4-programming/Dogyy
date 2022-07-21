package com.bnb.doggydoo.adoption.datasource.repo

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteAdoptionDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch AllAdoption Response from network
     * */
    suspend fun fetchAdoptionResponse(
        user_id: String,
        latitude: String,
        longitude: String
    ) =
        getResult {
            apiFactory.createService(AdoptionApiService::class.java)
                .getAdoptionList(
                    user_id, latitude, longitude
                )
        }


    suspend fun fetchAllAdoptionResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(AdoptionViewAllApiService::class.java)
                .getAllAdoptionList(
                    user_id
                )
        }

    suspend fun fetchAllShelterResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(ShelterApiService::class.java)
                .getShelterList(
                    user_id
                )
        }

    suspend fun fetchShelterDetailResponse(
        shelter_id: String
    ) =
        getResult {
            apiFactory.createService(ShelterDetailApi::class.java)
                .getShelterDetail(
                    shelter_id
                )
        }

    suspend fun fetchShelterDetailViewAllResponse(
        shelter_id: String
    ) =
        getResult {
            apiFactory.createService(ViewAllShelterDogAPI::class.java)
                .getSheltersDog(
                    shelter_id
                )
        }

}
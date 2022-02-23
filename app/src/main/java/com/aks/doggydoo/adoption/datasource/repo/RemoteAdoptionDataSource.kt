package com.aks.doggydoo.adoption.datasource.repo
import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteAdoptionDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch AllAdoption Response from network
     * */
    suspend fun fetchAdoptionResponse(
        user_id:String,
        latitude:String,
        longitude:String
    ) =
        getResult {
            apiFactory.createService(AdoptionApiService::class.java)
                .getAdoptionList(
                    user_id, latitude, longitude
                )
        }


    suspend fun fetchAllAdoptionResponse(
        user_id:String
    ) =
        getResult {
            apiFactory.createService(AdoptionViewAllApiService::class.java)
                .getAllAdoptionList(
                    user_id
                )
        }

    suspend fun fetchAllShelterResponse(
        user_id:String
    ) =
        getResult {
            apiFactory.createService(ShelterApiService::class.java)
                .getShelterList(
                    user_id
                )
        }

}
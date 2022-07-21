package com.bnb.doggydoo.playdate.datasource.repo

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemotePlayDate @Inject constructor(private var apiFactory: ApiFactory) : ResponseHandler() {

    /**
     * fetch home Response from network
     * */
    suspend fun fetchHomeResponse(
        userId: String,
        latitude:String,
        longitude:String
    ) =
        getResult {
            apiFactory.createService(PlayDateApiService::class.java)
                .getHomeResponse(
                    userId,latitude,longitude
                )
        }

    /**
     * fetch sent request Response from network
     * */
    suspend fun sentRequestResponse(
        userId: String,
        receive_id: String,
        petId: String,
        playdate: String,
        playtime: String,
        dateMode: String,
        user_pet_id:String
    ) =
        getResult {
            apiFactory.createService(PlayDateApiService::class.java)
                .sendRequest(
                    userId, receive_id, petId, playdate, playtime, dateMode,user_pet_id
                )
        }

    /**
     * fetch getPetDescription
     * Response from network
     * */
    suspend fun getPetDescriptionResponse(
        userId: String,
        petId: String
    ) =
        getResult {
            apiFactory.createService(PlayDateApiService::class.java)
                .getPetDescription(
                    userId, petId
                )
        }


    suspend fun getAllPetData(
        userId: String,
        type: String,
        latitude:String,
        longitude:String
    ) =
        getResult {
            apiFactory.createService(PlayDateApiService::class.java)
                .getAllPetDescription(
                    userId, type,latitude,longitude
                )
        }

    suspend fun getAllMyPet(
        user_id: String,
    ) =
        getResult {
            apiFactory.createService(PlayDateApiService::class.java)
                .getAllPet(
                    user_id
                )
        }
}
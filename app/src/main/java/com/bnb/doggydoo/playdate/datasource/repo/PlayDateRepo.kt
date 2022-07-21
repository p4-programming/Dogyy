package com.bnb.doggydoo.playdate.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayDateRepo @Inject constructor(private var remotePlayDate: RemotePlayDate) {

    fun getHomeResponse(
        userId: String,
        latitude: String,
        longitude: String
    ) = resultLiveData(
        networkCall = { remotePlayDate.fetchHomeResponse(userId,latitude,longitude) }
    )

    fun getSentRequestResponse(
        userId: String,
        receive_id: String,
        petId: String,
        playdate: String,
        playtime: String,
        dateMode: String,
        user_pet_id:String
    ) = resultLiveData(
        networkCall = {
            remotePlayDate.sentRequestResponse(
                userId,
                receive_id,
                petId,
                playdate,
                playtime,
                dateMode,
                user_pet_id
            )
        }
    )

    fun getPetDescriptionResponse(
        userId: String,
        petId: String
    ) = resultLiveData(
        networkCall = {
            remotePlayDate.getPetDescriptionResponse(
                userId,
                petId
            )
        }
    )


    fun getAllPetDataResponse(
        userId: String,
        type: String,
        latitude:String,
        longitude:String
    ) = resultLiveData(
        networkCall = {
            remotePlayDate.getAllPetData(
                userId,
                type,latitude,longitude
            )
        }
    )


    fun getAllMyPetDataResponse(
        user_id: String,
    ) = resultLiveData(
        networkCall = {
            remotePlayDate.getAllMyPet(
                user_id
            )
        }
    )
}
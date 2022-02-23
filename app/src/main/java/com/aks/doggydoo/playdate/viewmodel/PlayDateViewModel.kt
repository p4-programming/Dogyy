package com.aks.doggydoo.playdate.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.playdate.datasource.repo.PlayDateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayDateViewModel @Inject constructor(private var playDateRepo: PlayDateRepo) : ViewModel() {

    fun getHomeData(
        userId: String,
        latitude: String,
        longitude: String
    ) =
        playDateRepo.getHomeResponse(userId,latitude,longitude)

    fun sentRequestData(
        userId: String,
        receive_id: String,
        petId: String,
        playdate: String,
        playtime: String,
        dateMode: String,
        user_pet_id:String
    ) =
        playDateRepo.getSentRequestResponse(userId, receive_id, petId, playdate, playtime, dateMode,user_pet_id)

    fun getPetDescriptionData(
        userId: String,
        petId: String
    ) =
        playDateRepo.getPetDescriptionResponse(userId, petId)

    fun getAllPet(
        userId: String,
        type: String,
        latitude:String,
        longitude:String
    ) =
        playDateRepo.getAllPetDataResponse(userId, type,latitude,longitude)


    fun getAllMyPet(
        user_id: String
    ) =
        playDateRepo.getAllMyPetDataResponse(user_id)

}
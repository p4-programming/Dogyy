package com.bnb.doggydoo.adoptdogdetails.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.adoptdogdetails.datasource.repo.AdoptionDogDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdoptionDogDetailViewModel @Inject constructor(private var adoptionDogDetailRepo: AdoptionDogDetailRepo) :
    ViewModel() {

    fun getAdoptionDogDetailList(adoptId: String, userId: String) =
        adoptionDogDetailRepo.getAdoptionDogDetailLiveData(adoptId,userId)

    fun sendAdoption(
        userId: String,
        receiveId: String,
        petId: String
    ) =
        adoptionDogDetailRepo.getSendAdoptionLiveData(userId, receiveId, petId)

}
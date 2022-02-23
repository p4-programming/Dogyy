package com.aks.doggydoo.dogsitting.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.dogsitting.datasource.repo.DogsittingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogsittingModel @Inject constructor(var dogsittingRepo: DogsittingRepo) : ViewModel() {
    fun getDogsittingHomeList(user_id: String, latitude: String, longitude: String) =
        dogsittingRepo.getDogsittingLiveData(user_id, latitude, longitude)

    fun getAllDogsittingList(user_id: String, type: String, latitude: String, longitude: String) =
        dogsittingRepo.getAllDogsittingLiveData(user_id, type, latitude, longitude)

    fun sendAllDogsitReq(user_id: String,recive_id:String, pet_id:String, dogsitdate:String,dogsittime:String) =
        dogsittingRepo.getAllDogsitReqLiveData(user_id,recive_id, pet_id,dogsitdate,dogsittime)

    fun getAllDogList(user_id: String) =
        dogsittingRepo.getAllDogLiveData(user_id)

    fun rateHeroUser(userid: String, heroid:String,rate:String,feedback:String) =
        dogsittingRepo.getRateHeroLiveData(userid,heroid,rate,feedback)
}
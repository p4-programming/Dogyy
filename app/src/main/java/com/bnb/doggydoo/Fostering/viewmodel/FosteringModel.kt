package com.bnb.doggydoo.fostering.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.fostering.datasource.repo.FosteringRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FosteringModel @Inject constructor(var fosteringRepo: FosteringRepo) : ViewModel() {
    fun getFosteringHomeList(
        user_id: String, latitude: String,
        longitude: String
    ) = fosteringRepo.getFosteringLiveData(user_id, latitude, longitude)

    fun getFosteringDetail(fosterid: String, userid: String) =
        fosteringRepo.getFosteringLiveDataDetail(fosterid, userid)

    fun sendFosteringRequest( type: String ,user_id: String,recieve_id: String, fost_pet_id: String) =
        fosteringRepo.sentFosteringReqLiveDataDetail(type,user_id,recieve_id,fost_pet_id)

    fun getAllFosteringRequest(user_id: String, type: String,latitude:String, longitude:String) =
        fosteringRepo.getAllFosteringLiveDataDetail(user_id, type,latitude,longitude)
}
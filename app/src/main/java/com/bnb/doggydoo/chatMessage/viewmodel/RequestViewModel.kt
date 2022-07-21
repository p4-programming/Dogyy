package com.bnb.doggydoo.chatMessage.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.chatMessage.datasource.repo.request.RequestRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(var requestRepo: RequestRepo) :
    ViewModel() {
    fun sentRequestData(
        userid: String
    ) =
        requestRepo.sentRequestLiveData(userid)

    fun receiveRequestData(
        userid: String
    ) =
        requestRepo.receiveRequestLiveData(userid)

    fun acceptOrReject(
        request_id: String,
        requestType:String,
        request_status: String,
        user_id: String,
        pet_id: String
    ) =
        requestRepo.acceptOrRejectLiveData(request_id,requestType,request_status,user_id,pet_id)
}
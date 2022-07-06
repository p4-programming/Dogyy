package com.aks.doggydoo.chatMessage.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.chatMessage.datasource.repo.request.RequestRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallListViewModel @Inject constructor(var requestRepo: RequestRepo) :
    ViewModel() {
    fun callListData(
        userid: String
    ) =
        requestRepo.callListLiveData(userid)
}
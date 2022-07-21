package com.bnb.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.callawet.datasource.repo.callreason.CallReasonRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallReasonModel @Inject constructor(var callReasonRepo: CallReasonRepo) : ViewModel() {
    fun getCallReason() =
        callReasonRepo.callReasonLiveData()
}
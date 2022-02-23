package com.aks.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.callawet.datasource.repo.callreason.CallReasonRepo
import com.aks.doggydoo.callawet.datasource.repo.rate.RateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallReasonModel @Inject constructor(var callReasonRepo: CallReasonRepo) : ViewModel() {
    fun getCallReason() =
        callReasonRepo.callReasonLiveData()
}
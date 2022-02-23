package com.aks.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.callawet.datasource.repo.rate.RateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateDoctorModel @Inject constructor(var rateRepo: RateRepo) : ViewModel() {
    fun getRateData(  callid: String,
                      rate: String,
                      feedback: String) =
        rateRepo.rateLiveData(callid, rate, feedback)
}
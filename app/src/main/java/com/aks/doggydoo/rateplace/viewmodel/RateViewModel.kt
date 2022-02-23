package com.aks.doggydoo.rateplace.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.onboarding.datasource.repo.OnBoardingRepo
import com.aks.doggydoo.rateplace.datasource.repo.RateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateViewModel @Inject constructor(var rateRepo: RateRepo) : ViewModel() {
    fun getRateData(  user_id: String,
                      park_id: String,
                      rate: String,
                      review: String,
                      pet_friendly: String) =
        rateRepo.rateLiveData(user_id, park_id, rate, review, pet_friendly)
}
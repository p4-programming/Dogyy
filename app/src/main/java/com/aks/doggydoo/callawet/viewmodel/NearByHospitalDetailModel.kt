package com.aks.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.callawet.datasource.repo.nearbyhos.NearByHosDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NearByHospitalDetailModel @Inject constructor(var nearByHosDetailRepo: NearByHosDetailRepo) : ViewModel() {
    fun fetHospitalDetailList(clinic_id:String,user_id: String) = nearByHosDetailRepo.getHospitalDetailLiveData(clinic_id,user_id)
}
package com.aks.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.callawet.datasource.repo.nearbydoc.NearByDoctorDetailRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NearByDoctorDetailModel @Inject constructor(var doctorDetailRepo: NearByDoctorDetailRepo) : ViewModel() {
    fun fetDoctorDetailList(vetid:String,userid: String) = doctorDetailRepo.getDoctorDetailLiveData(vetid,userid)
}
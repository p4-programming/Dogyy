package com.bnb.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.callawet.datasource.repo.home.CallaWetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallaWetModel  @Inject constructor(var callawetRepo: CallaWetRepo) : ViewModel() {
    fun getCallaWetHomeList(userid: String,latitude:String,
                            longitude:String) = callawetRepo.getCallaWetListLiveData(userid,latitude,longitude)

    fun getAllClinicList(userid: String) = callawetRepo.getAllClinicLiveData(userid)
    fun getAllDocList(userid: String) = callawetRepo.getAllDocLiveData(userid)
}
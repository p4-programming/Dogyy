package com.aks.doggydoo.parkdescription.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.parkdescription.datasource.repo.PetDescriptionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PetDescriptionViewModel @Inject constructor(var petDescriptionRepo: PetDescriptionRepo) :
    ViewModel() {
    fun getParkDescriptionData(
        park_id: String, user_id: String
    ) =
        petDescriptionRepo.getParkDescriptionLiveData(park_id,user_id)

    fun getCheckInData(
        type: String, park_id: String, userid:String
    ) =
        petDescriptionRepo.getCheckInLiveData(type, park_id, userid)
}
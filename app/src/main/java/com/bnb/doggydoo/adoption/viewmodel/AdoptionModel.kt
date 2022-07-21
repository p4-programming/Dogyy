package com.bnb.doggydoo.adoption.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.adoption.datasource.repo.AdoptionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdoptionModel @Inject constructor(private var adoptionRepo: AdoptionRepo) : ViewModel() {
    fun getAdoptionList(
        user_id: String,
        latitude: String,
        longitude: String
    ) = adoptionRepo.getAdoptionListLiveData(user_id, latitude, longitude)

    fun getAllAdoptionList(user_id: String) = adoptionRepo.getAllAdoptionListLiveData(user_id)
    fun getAllShelterList(user_id: String) = adoptionRepo.getAllShelterListLiveData(user_id)
    fun getShelterDetail(shelter_id: String) = adoptionRepo.getAllShelterDetailLiveData(shelter_id)
    fun getShelterDetailViewAll(shelter_id: String) = adoptionRepo.getAllShelterdetailViewAllLiveData(shelter_id)

}
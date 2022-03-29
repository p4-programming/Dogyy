package com.aks.doggydoo.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.onboarding.datasource.repo.OnBoardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(var onBoardingRepo: OnBoardingRepo) : ViewModel() {

    fun getPetBreedData() =
        onBoardingRepo.petBreedLiveData()

    fun getFosterData(userId: String) =
        onBoardingRepo.petFosterLiveData(userId)

    fun getDogSittingData(
        userId: String, date: String,
        startTime: String,
        endTime: String
    ) =
        onBoardingRepo.petDogSittingLiveData(userId, date, startTime, endTime)

    fun getUserData(
        userId: String,
        name: String?,
        username: String,
        age: String?,
        description: String?,
        profile: MultipartBody.Part
    ) =
        onBoardingRepo.userLiveData(userId, name!!,username, age!!, description, profile)

    fun getDogData(
        userId: String,
        name: String,
        age: String,
        age_month: String,
        ageType: String,
        pet_breed: String,
        pet_gender: String,
        pet_weight: String,
        pet_weight_gm: String,
        pet_weight_type: String,
        pet_medical_conditions: String,
        is_pet_vaccinated: String,
        description: String?,
        lattitude: String,
        longitude:String,
        pet_image: MultipartBody.Part
    ) =
        onBoardingRepo.petLiveData(
            userId,
            name,
            age,
            age_month,
            ageType,
            pet_breed,
            pet_gender,
            pet_weight,
            pet_weight_gm,
            pet_weight_type,
            pet_medical_conditions,
            is_pet_vaccinated,
            description,
            lattitude,
            longitude,
            pet_image
        )

    fun getUserName(username: String, user_id: String) =
        onBoardingRepo.userNameLiveData(username, user_id)
}
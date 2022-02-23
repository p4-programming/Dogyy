package com.aks.doggydoo.onboarding.datasource.repo

import com.aks.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnBoardingRepo @Inject
constructor(
    private var remoteOnBoardingDataSource: RemoteOnBoardingDataSource) {

    fun petBreedLiveData() = resultLiveData(
        networkCall = { remoteOnBoardingDataSource.fetchPetBreedResponse() }
    )

    fun petFosterLiveData(userId: String) = resultLiveData(
        networkCall = { remoteOnBoardingDataSource.fetchFosterResponse(userId) }
    )

    fun petDogSittingLiveData(
        userId: String,
        date: String,
        startTime: String,
        endTime: String
    ) = resultLiveData(
        networkCall = {
            remoteOnBoardingDataSource.fetchDogSittingResponse(
                userId,
                date,
                startTime,
                endTime
            )
        }
    )

    fun userLiveData(
        userId: String,
        name: String,
        username: String,
        age: String,
        description: String?,
        profile: MultipartBody.Part
    ) = resultLiveData(
        networkCall = {
            remoteOnBoardingDataSource.fetchUserResponse(
                userId,
                name,
                username,
                age,
                description,
                profile
            )
        }
    )

    fun petLiveData(
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
    ) = resultLiveData(
        networkCall = {
            remoteOnBoardingDataSource.fetchPetResponse(
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
        }
    )


    fun userNameLiveData(
        username: String,
        user_id: String
    ) = resultLiveData(
        networkCall = {
            remoteOnBoardingDataSource.fetchUserNameResponse(
                username,
                user_id
            )
        }
    )
}
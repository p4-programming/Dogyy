package com.aks.doggydoo.onboarding.datasource.repo

import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteOnBoardingDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch PetBreed Response from network
     * */
    suspend fun fetchPetBreedResponse() =
        getResult {
            apiFactory.createService(OnBoardingApiService::class.java)
                .getPetBreed()
        }

    /**
     * fetch UserInfo Response from network
     * */
    suspend fun fetchUserResponse(
        userId: String,
        name: String,
        username: String,
        age: String,
        description: String?,
        profile: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(OnBoardingApiService::class.java)
                .userRegister(
                    MultipartFile.createPartFromString(userId),
                    MultipartFile.createPartFromString(name),
                    MultipartFile.createPartFromString(username),
                    MultipartFile.createPartFromString(age),
                    MultipartFile.createPartFromString(description),
                    profile
                )
        }

    /**
     * fetch DogInfo Response from network
     * */
    suspend fun fetchPetResponse(
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
    ) = getResult {
        apiFactory.createService(OnBoardingApiService::class.java)
            .petRegister(
                user_id = MultipartFile.createPartFromString(userId),
                pet_name = MultipartFile.createPartFromString(name),
                age = MultipartFile.createPartFromString(age),
                pet_age_month = MultipartFile.createPartFromString(age_month),
                ageType = MultipartFile.createPartFromString(ageType),
                breed = MultipartFile.createPartFromString(pet_breed),
                gender = MultipartFile.createPartFromString(pet_gender),
                weight = MultipartFile.createPartFromString(pet_weight),
                pet_weight_gm = MultipartFile.createPartFromString(pet_weight_gm),
                weightType = MultipartFile.createPartFromString(pet_weight_type),
                description = MultipartFile.createPartFromString(description),
                medCondition = MultipartFile.createPartFromString(pet_medical_conditions),
                is_pet_vaccinated = MultipartFile.createPartFromString(is_pet_vaccinated),
                lattitude = MultipartFile.createPartFromString(lattitude),
                longitude = MultipartFile.createPartFromString(longitude),
                pet_image
            )
    }

    /**
     * fetch Fostering Response from network
     * */
    suspend fun fetchFosterResponse(userId: String) =
        getResult {
            apiFactory.createService(OnBoardingApiService::class.java)
                .insertFostering(userId)
        }

    /**
     * fetch DogSitting Response from network
     * */
    suspend fun fetchDogSittingResponse(
        userId: String,
        date: String,
        startTime: String,
        endTime: String
    ) =
        getResult {
            apiFactory.createService(OnBoardingApiService::class.java)
                .insertDogSitting(userId,date,startTime,endTime)
        }


    /**
     * fetch DogSitting Response from network
     * */
    suspend fun fetchUserNameResponse(
        username: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(OnBoardingApiService::class.java)
                .userNameAvailability(username,user_id)
        }

}
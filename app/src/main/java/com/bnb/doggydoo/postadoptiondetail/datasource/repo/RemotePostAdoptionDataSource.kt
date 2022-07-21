package com.bnb.doggydoo.postadoptiondetail.datasource.repo

import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemotePostAdoptionDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch postAdoption Response from network
     * */
    suspend fun fetchRateListResponse(
        name: String,
        age: String,
        condition: String,
        breed: String,
        about: String,
        vaccinated: String,
        pet1Base64: List<MultipartBody.Part>,
        userId: String,
        userName: String,
        userEmail: String,
        userMobile: String,
        userPhoto1Base64: List<MultipartBody.Part>,
        ageType: String,
        shelter_id: String,
        living_type: String
    ) =
        getResult {
            apiFactory.createService(PostAdoptionApiService::class.java)
                .postAdoption(
                    name = MultipartFile.createPartFromString(name),
                    age = MultipartFile.createPartFromString(age),
                    age_type = MultipartFile.createPartFromString(ageType),
                    breed = MultipartFile.createPartFromString(breed),
                    about_dog = MultipartFile.createPartFromString(about),
                    vaccinated = MultipartFile.createPartFromString(vaccinated),
                    medical_condition = MultipartFile.createPartFromString(condition),
                    pet_pic = pet1Base64,
                    user_id = MultipartFile.createPartFromString(userId),
                    user_name = MultipartFile.createPartFromString(userName),
                    user_email = MultipartFile.createPartFromString(userEmail),
                    user_mobile = MultipartFile.createPartFromString(userMobile),
                    shelter_id = MultipartFile.createPartFromString(shelter_id),
                    living_type = MultipartFile.createPartFromString(living_type),
                    user_photo_id = userPhoto1Base64
                )
        }
}
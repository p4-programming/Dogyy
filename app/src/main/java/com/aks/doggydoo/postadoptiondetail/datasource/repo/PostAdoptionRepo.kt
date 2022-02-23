package com.aks.doggydoo.postadoptiondetail.datasource.repo

import com.aks.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostAdoptionRepo @Inject
constructor(
    private var postAdoptionDataSource: RemotePostAdoptionDataSource
) {
    fun getPostAdoptionResponseLiveData(
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
    ) = resultLiveData(
        networkCall = {
            postAdoptionDataSource.fetchRateListResponse(
                name,
                age,
                condition,
                breed,
                about,
                vaccinated,
                pet1Base64,
                userId,
                userName,
                userEmail,
                userMobile,
                userPhoto1Base64,
                ageType,shelter_id, living_type
            )
        }
    )
}
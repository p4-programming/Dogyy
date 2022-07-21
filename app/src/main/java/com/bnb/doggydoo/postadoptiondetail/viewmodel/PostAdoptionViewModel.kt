package com.bnb.doggydoo.postadoptiondetail.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.postadoptiondetail.datasource.repo.PostAdoptionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PostAdoptionViewModel @Inject constructor(private var postAdoptionRepo: PostAdoptionRepo) :
    ViewModel() {
    fun getPostAdoptionData(
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
        postAdoptionRepo.getPostAdoptionResponseLiveData(
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
            userPhoto1Base64, ageType,shelter_id, living_type
        )
}
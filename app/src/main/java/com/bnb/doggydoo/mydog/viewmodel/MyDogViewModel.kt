package com.bnb.doggydoo.mydog.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.mydog.datasource.repo.MyDogRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyDogViewModel @Inject constructor(var dogRepo: MyDogRepo) : ViewModel() {

    fun getUploadProfileImageData(petId: String, prepareFilePart: MultipartBody.Part) =
        dogRepo.updateDogProfileImageLiveData(petId, prepareFilePart)

    fun getPetDescriptionData(pet_id: String, user_id: String) =
        dogRepo.updatePetDescriptionPostLiveData(pet_id, user_id)


    fun getPetDescriptionDatas(user_id: String) =
        dogRepo.getalldistresspetbyuserid(user_id)

    fun getPostReminderData(
        petId: String,
        petTypeId: String,
        reminder_date: String,
        other: String,
        user_id:String
    ) =
        dogRepo.updatePetReminderPostLiveData(petId, petTypeId, reminder_date, other,user_id)

    fun getPostPetDocumentData(
        petId: String,
        caption: String,
        document: MultipartBody.Part
    ) =
        dogRepo.updatePetDocumentLiveData(petId, caption, document)

    fun getEditPetData(
        pet_id: String,
        pet_name: String,
        pet_age: String,
        pet_age_month: String,
        pet_breed: String,
        pet_gender: String,
        pet_weight: String,
        pet_weight_gm: String,
        pet_description: String,
        is_pet_vaccinated: String,
        pet_medical_condition:String,
        profile: MultipartBody.Part
    ) =
        dogRepo.editPetLiveData(
            pet_id,
            pet_name,
            pet_age,
            pet_age_month,
            pet_breed,
            pet_gender,
            pet_weight,
            pet_weight_gm,
            pet_description,
            is_pet_vaccinated,
            pet_medical_condition,
            profile
        )

    fun getPetStatusUpdateData(
        pet_id: String,
        status: String,
        type: String,
        start_date: String,
        start_time: String,
        end_date: String,
        end_time: String
    ) =
        dogRepo.updatePetStatusLiveData(pet_id, status, type,start_date,
            start_time,
            end_date,
            end_time)


    fun getPetDeleteStatusData(
        reminder_id: String, user_id: String
    ) =
        dogRepo.deletePetPostLiveData(reminder_id,user_id)

    fun getDistressPetData(
        user_id: String,
        pet_description: String,
        lattitute: String,
        longitute: String,
        profile: MultipartBody.Part,
        type: String
    ) =
        dogRepo.distressPetPostLiveData(user_id,pet_description,lattitute,longitute,
            profile,type)

    fun getDistressPetDetailData(
        pet_id: String
    ) =
        dogRepo.distressPetPostLiveData(pet_id)
}
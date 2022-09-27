package com.bnb.doggydoo.mydog.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDogRepo @Inject
constructor(
    private var remoteMyDogDataSource: RemoteMyDogDataSource
) {
    fun updateDogProfileImageLiveData(userId: String, prepareFilePart: MultipartBody.Part) =
        resultLiveData(
            networkCall = {
                remoteMyDogDataSource.fetchDogProfileUpdateResponse(
                    userId,
                    prepareFilePart
                )
            }
        )

    fun updatePetReminderPostLiveData(
        petId: String,
        petTypeId: String,
        reminder_date: String,
        other: String,
        user_id:String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchPetReminderPostResponse(
                petId,
                petTypeId,
                reminder_date,
                other,
                user_id
            )
        }
    )
    fun updatePetDescriptionPostLiveData(
        pet_id: String,user_id:String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchPetDescriptionResponse(
                pet_id, user_id
            )
        }
    )

    fun getalldistresspetbyuserid(
        user_id:String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.getalldistresspetbyuserid(
                user_id
            )
        }
    )


 fun updatePetDocumentLiveData(
     petId: String,
     caption: String,
     document: MultipartBody.Part
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchPetDocumentResponse(
                petId, caption, document
            )
        }
    )

    fun editPetLiveData(
        pet_id: String,
        pet_name: String,
        pet_age: String,
        pet_age_month: String,
        pet_breed: String,
        pet_gender: String,
        pet_weight: String,
        pet_weight_gm: String,
        pet_description: String,
        is_pet_vaccinated:String,
        pet_medical_condition:String,
        profile: MultipartBody.Part
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchPetEditProfileInfoResponse(
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
        }
    )

    fun updatePetStatusLiveData(
        pet_id: String,
        status: String,
        type: String,
        start_date: String,
        start_time: String,
        end_date: String,
        end_time: String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchPetUpdateStatusResponse(
                pet_id, status,type,start_date,
                start_time,
                end_date,
                end_time
            )
        }
    )


    fun deletePetPostLiveData(
        reminder_id: String, user_id: String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchDeletePetResponse(
                reminder_id,user_id
            )
        }
    )


    fun distressPetPostLiveData(
        user_id: String,
        pet_description: String,
        lattitute: String,
        longitute: String,
        profile: MultipartBody.Part,
        type: String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.distressPinPetResponse(
                user_id,pet_description,lattitute,longitute,
                profile,type
            )
        }
    )


    fun distressPetPostLiveData(
        pet_id: String
    ) = resultLiveData(
        networkCall = {
            remoteMyDogDataSource.fetchDistressPetResponse(
                pet_id
            )
        }
    )
}
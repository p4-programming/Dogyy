package com.bnb.doggydoo.mydog.datasource.repo

import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteMyDogDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch DogProfileUpdate Response from network
     * */
    suspend fun fetchDogProfileUpdateResponse(
        petId: String,
        prepareFilePart: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .dogProfileUpdate(
                    pet_id = MultipartFile.createPartFromString(petId),
                    user_profile = prepareFilePart
                )
        }

    /**
     * fetch PetReminder Response from network
     * */
    suspend fun fetchPetReminderPostResponse(
        petId: String,
        petTypeId: String,
        reminder_date: String,
        other: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .setPetReminder(petId, petTypeId, reminder_date, other, user_id)
        }

    /**
     * fetch PetDescription Response from network
     * */
    suspend fun fetchPetDescriptionResponse(
        pet_id: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .getPetDescription(pet_id, user_id)
        }



    suspend fun getalldistresspetbyuserid(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .getAllDistressPetByUserid(user_id)
        }



    /**
     * fetch PetDocument Response from network
     * */
    suspend fun fetchPetDocumentResponse(
        petId: String,
        caption: String,
        prepareFilePart: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .uploadPetDocument(
                    MultipartFile.createPartFromString(petId),
                    MultipartFile.createPartFromString(caption),
                    petDocument = prepareFilePart
                )
        }

    suspend fun fetchPetEditProfileInfoResponse(
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
        pet_medical_condition: String,
        profile: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .editPetProfile(
                    MultipartFile.createPartFromString(pet_id),
                    MultipartFile.createPartFromString(pet_name),
                    MultipartFile.createPartFromString(pet_age),
                    MultipartFile.createPartFromString(pet_age_month),
                    MultipartFile.createPartFromString(pet_breed),
                    MultipartFile.createPartFromString(pet_gender),
                    MultipartFile.createPartFromString(pet_weight),
                    MultipartFile.createPartFromString(pet_weight_gm),
                    MultipartFile.createPartFromString(pet_description),
                    MultipartFile.createPartFromString(is_pet_vaccinated),
                    MultipartFile.createPartFromString(pet_medical_condition),
                    profile
                )
        }

    suspend fun fetchPetUpdateStatusResponse(
        pet_id: String,
        status: String,
        type: String,
        start_date: String,
        start_time: String,
        end_date: String,
        end_time: String

    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .getUpdatePetStatus(
                    pet_id,
                    status,
                    type,
                    start_date,
                    start_time,
                    end_date,
                    end_time
                )
        }


    suspend fun fetchDeletePetResponse(
        reminder_id: String, user_id: String
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .deletePetReminder(reminder_id, user_id)
        }


    suspend fun distressPinPetResponse(
        user_id: String,
        pet_description: String,
        lattitute: String,
        longitute: String,
        type: String,
        notificationType: String,
        profile: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .distresPinPetProfile(
                    MultipartFile.createPartFromString(user_id),
                    MultipartFile.createPartFromString(pet_description),
                    MultipartFile.createPartFromString(lattitute),
                    MultipartFile.createPartFromString(longitute),
                    MultipartFile.createPartFromString(type),
                    MultipartFile.createPartFromString(notificationType),
                    profile
                )
        }


    suspend fun fetchDistressPetResponse(
        pet_id: String
    ) =
        getResult {
            apiFactory.createService(MyDogApiService::class.java)
                .distressPetDetail(pet_id)
        }
}
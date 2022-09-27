package com.bnb.doggydoo.mydog.datasource.repo

import com.bnb.doggydoo.mydog.datasource.model.EditPetProfileResponse
import com.bnb.doggydoo.mydog.datasource.model.PetDocResponse
import com.bnb.doggydoo.mydog.datasource.model.PetReminderResponse
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.DistressPetResponse
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.myprofile.datasource.model.UserImageUploadResponse
import com.bnb.doggydoo.myprofile.datasource.model.profile.UpdateStatusResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MyDogApiService {

    /**
     * Upload image
     */
    @Multipart
    @POST("AuthApi/PeImageUpdate")
    suspend fun dogProfileUpdate(
        @Part("pet_id") pet_id: RequestBody,
        @Part user_profile: MultipartBody.Part
    ): Response<UserImageUploadResponse>

    /**
     * Post reminder
     */
    @FormUrlEncoded
    @POST("AuthApi/setpetreminder")
    suspend fun setPetReminder(
        @Field("petid") pet_id: String,
        @Field("pettypeid") petTypeId: String,
        @Field("reminder_date") reminder_date: String,
        @Field("othervalue") other: String,
        @Field("user_id") user_id: String,
    ): Response<PetReminderResponse>

    /**
     * Get Dog's Description
     */
    @FormUrlEncoded
    @POST("AuthApi/getPetdescription")
    suspend fun getPetDescription(
        @Field("pet_id") pet_id: String,
        @Field("user_id") user_id: String
    ): Response<PetDescriptionResponse>



    @FormUrlEncoded
    @POST("AuthApi/getalldistresspetbyuserid")
    suspend fun getAllDistressPetByUserid(
        @Field("user_id") user_id: String
    ): Response<getDistressPinByUserID>


    /**
     * Get Dog's Document
     */
    @Multipart
    @POST("AuthApi/uploadpetdocument")
    suspend fun uploadPetDocument(
        @Part("petid") pet_id: RequestBody,
        @Part("caption") caption: RequestBody,
        @Part petDocument: MultipartBody.Part,
    ): Response<PetDocResponse>

    @Multipart
    @POST("AuthApi/petUpdated")
    suspend fun editPetProfile(
        @Part("pet_id") pet_id: RequestBody,
        @Part("pet_name") pet_name: RequestBody,
        @Part("pet_age") pet_age: RequestBody,
        @Part("pet_age_month") pet_age_month: RequestBody,
        @Part("pet_breed") pet_breed: RequestBody,
        @Part("pet_gender") pet_gender: RequestBody,
        @Part("pet_weight") pet_weight: RequestBody,
        @Part("pet_weight_gm") pet_weight_gm: RequestBody,
        @Part("pet_description") pet_description: RequestBody,
        @Part("is_pet_vaccinated") is_pet_vaccinated: RequestBody,
        @Part("pet_medical_condition") pet_medical_condition: RequestBody,
        @Part pet_image: MultipartBody.Part
    ): Response<EditPetProfileResponse>


    @FormUrlEncoded
    @POST("AuthApi/dogstatusupdate")
    suspend fun getUpdatePetStatus(
        @Field("pet_id") pet_id: String,
        @Field("status") status: String,
        @Field("type") type: String,
        @Field("start_date") start_date: String,
        @Field("start_time") start_time: String,
        @Field("end_date") end_date: String,
        @Field("end_time") end_time: String

    ): Response<UpdateStatusResponse>


    @FormUrlEncoded
    @POST("AuthApi/deletepetreminder")
    suspend fun deletePetReminder(
        @Field("reminder_id") reminder_id: String,
        @Field("user_id") user_id: String
    ): Response<PetReminderResponse>

    @Multipart
    @POST("AuthApi/DisrtressPetRegister")
    suspend fun distresPinPetProfile(
        @Part("user_id") user_id: RequestBody,
        @Part("pet_description") pet_name: RequestBody,
        @Part("lattitute") lattitute: RequestBody,
        @Part("longitute") longitute: RequestBody,
        @Part pet_image: MultipartBody.Part,
        @Part("type") type: RequestBody,
    ): Response<PetReminderResponse>

    @FormUrlEncoded
    @POST("AuthApi/getdistresspetdescription")
    suspend fun distressPetDetail(
        @Field("pet_id") pet_id: String
    ): Response<DistressPetResponse>

}
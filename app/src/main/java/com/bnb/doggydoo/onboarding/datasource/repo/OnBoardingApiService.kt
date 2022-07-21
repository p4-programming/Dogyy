package com.bnb.doggydoo.onboarding.datasource.repo

import com.bnb.doggydoo.article.datasource.model.InsertArticleResponse
import com.bnb.doggydoo.onboarding.datasource.model.pet.PetBreedResponse
import com.bnb.doggydoo.onboarding.datasource.model.user.UserNameResponse
import com.bnb.doggydoo.onboarding.datasource.model.user.UserRegistrationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface OnBoardingApiService {

    /**
     * Get pet breed
     */
    @POST("AuthApi/getpetBreed")
    suspend fun getPetBreed(): Response<PetBreedResponse>

    /**
     * User registration
     */
    @Multipart
    @POST("AuthApi/userRegister")
    suspend fun userRegister(
        @Part("user_id") user_id: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("age") age: RequestBody,
        @Part("user_decription") userDescription: RequestBody,
        @Part user_profile: MultipartBody.Part
    ): Response<UserRegistrationResponse>

    /**
     * Pet registration
     */
    @Multipart
    @POST("AuthApi/petRegister")
    suspend fun petRegister(
        @Part("user_id") user_id: RequestBody,
        @Part("pet_name") pet_name: RequestBody,
        @Part("pet_age") age: RequestBody,
        @Part("pet_age_month") pet_age_month: RequestBody,
        @Part("pet_age_type") ageType: RequestBody,
        @Part("pet_breed") breed: RequestBody,
        @Part("pet_gender") gender: RequestBody,
        @Part("pet_weight") weight: RequestBody,
        @Part("pet_weight_gm") pet_weight_gm: RequestBody,
        @Part("pet_weight_type") weightType: RequestBody,
        @Part("pet_description") description: RequestBody,
        @Part("pet_medical_conditions") medCondition: RequestBody,
        @Part("is_pet_vaccinated") is_pet_vaccinated: RequestBody,
        @Part("lattitute") lattitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part pet_image: MultipartBody.Part
    ): Response<PetBreedResponse>

    /**
     * insert if user is up for Fostering
     */
    @FormUrlEncoded
    @POST("AuthApi/fostering")
    suspend fun insertFostering(
        @Field("user_id") user_id: String
    ): Response<InsertArticleResponse>

    /**
     * insert if user is up for Fostering
     */
    @FormUrlEncoded
    @POST("AuthApi/dogsitting")
    suspend fun insertDogSitting(
        @Field("user_id") user_id: String,
        @Field("dogsitter_date") dogsitter_date: String,
        @Field("dogsitter_start_time") dogsitter_start_time: String,
        @Field("dogsitter_end_time") dogsitter_end_time: String,
    ): Response<InsertArticleResponse>


    //Username availability
    @FormUrlEncoded
    @POST("AuthApi/suggestusername")
    suspend fun userNameAvailability(
        @Field("username") username: String,
        @Field("user_id") user_id: String
    ): Response<UserNameResponse>
}
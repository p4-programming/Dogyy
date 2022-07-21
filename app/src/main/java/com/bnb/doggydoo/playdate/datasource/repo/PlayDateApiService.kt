package com.bnb.doggydoo.playdate.datasource.repo

import com.bnb.doggydoo.playdate.datasource.model.AllPetResponse
import com.bnb.doggydoo.playdate.datasource.model.PlayDateDetailResponse
import com.bnb.doggydoo.playdate.datasource.model.PlayDateSentRequestResponse
import com.bnb.doggydoo.playdate.datasource.model.PlayDateViewAllResponse
import com.bnb.doggydoo.playdate.datasource.model.homepage.PlayDateHomeResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PlayDateApiService {
    /**
     * Home page
     */
    @FormUrlEncoded
    @POST("HomeApi/PetplayHomepage")
    suspend fun getHomeResponse(
        @Field("user_id") user_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<PlayDateHomeResponse>

    /**
     * Send Request
     */
    @FormUrlEncoded
    @POST("HomeApi/petplaysend")
    suspend fun sendRequest(
        @Field("user_id") user_id: String,
        @Field("recive_id") receive_id: String,
        @Field("pet_id") pet_id: String,
        @Field("playdate") playdate: String,
        @Field("playtime") playtime: String,
        @Field("date_mode") date_mode: String,
        @Field("user_pet_id") user_pet_id: String
    ): Response<PlayDateSentRequestResponse>

    /**
     * PlaydateDetail
     */
    @FormUrlEncoded
    @POST("AuthApi/getPetplaydescription")
    suspend fun getPetDescription(
        @Field("user_id") user_id: String,
        @Field("pet_id") pet_id: String
    ): Response<PlayDateDetailResponse>


    @FormUrlEncoded
    @POST("HomeApi/ViewAllPetplay")
    suspend fun getAllPetDescription(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<PlayDateViewAllResponse>


    @FormUrlEncoded
    @POST("AuthApi/getMypet")
    suspend fun getAllPet(
        @Field("user_id") user_id: String,
    ): Response<AllPetResponse>
}

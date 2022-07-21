package com.bnb.doggydoo.dogsitting.datasource.repo

import com.bnb.doggydoo.dogsitting.datasource.model.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DogSittingHomeApiService {
    @FormUrlEncoded
    @POST("HomeApi/DogsitterHomepage")
    suspend fun getDogsitHomeDataList(
        @Field("user_id") user_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<DogSittingHomeResponse>


    @FormUrlEncoded
    @POST("HomeApi/dogsittviewall")
    suspend fun getDogsitAllList(
        @Field("user_id") user_id: String,
        @Field("type") type:String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<DogSittingAllResponse>


    @FormUrlEncoded
    @POST("HomeApi/Dogsittingsend")
    suspend fun sentDogsitRequest(
        @Field("user_id") user_id: String,
        @Field("recive_id") recive_id:String,
        @Field("pet_id") pet_id: String,
        @Field("dogsitdate") dogsitdate: String,
        @Field("dogsittime") dogsittime: String
    ): Response<DogsitRequestResponse>

    @FormUrlEncoded
    @POST("AuthApi/getMypet")
    suspend fun getDog(
        @Field("user_id") user_id: String,
    ): Response<AllDogResponse>


    @FormUrlEncoded
    @POST("VatApi/Ratetohero")
    suspend fun rateHeroUser(
        @Field("userid") user_id: String,
        @Field("heroid") heroid: String,
        @Field("rate") rate: String,
        @Field("feedback") feedback: String
    ): Response<RateHeroResponse>
}
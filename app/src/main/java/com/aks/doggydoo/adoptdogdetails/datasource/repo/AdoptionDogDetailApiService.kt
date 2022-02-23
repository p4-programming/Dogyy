package com.aks.doggydoo.adoptdogdetails.datasource.repo

import com.aks.doggydoo.adoptdogdetails.datasource.model.AdoptionDogDetailResponse
import com.aks.doggydoo.adoption.datasource.model.AdoptionListResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AdoptionDogDetailApiService {

    @FormUrlEncoded
    @POST("AdoptionApi/adoptiondetail")
    suspend fun getAdoptionDetail(
        @Field("adoptid") adoptid: String,
        @Field("userid") userid: String
    ): Response<AdoptionDogDetailResponse>

    @FormUrlEncoded
    @POST("AdoptionApi/adoptionsend")
    suspend fun sendAdoptionRequest(
        @Field("user_id") userId: String,
        @Field("recieve_id") receive_id: String,
        @Field("pet_id") pet_id: String
    ): Response<AdoptionDogDetailResponse>
}
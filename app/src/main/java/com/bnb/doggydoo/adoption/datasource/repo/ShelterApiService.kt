package com.bnb.doggydoo.adoption.datasource.repo

import com.bnb.doggydoo.adoption.datasource.model.ShelterListResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ShelterApiService {
    @FormUrlEncoded
    @POST("NewsfeedAPI/getallshelter")
    suspend fun getShelterList(
        @Field("user_id") user_id:String
    ): Response<ShelterListResponse>
}
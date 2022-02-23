package com.aks.doggydoo.fostering.datasource.repo

import com.aks.doggydoo.fostering.datasource.model.FosteringHomeResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface fosteringHomeApiService {
    @FormUrlEncoded
    @POST("ForestingApi/forsettingHome")
    suspend fun getFosterHomeDataList(
        @Field("user_id") user_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<FosteringHomeResponse>
}
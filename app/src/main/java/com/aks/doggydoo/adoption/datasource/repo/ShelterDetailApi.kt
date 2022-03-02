package com.aks.doggydoo.adoption.datasource.repo

import com.aks.doggydoo.adoption.datasource.model.ShelterDetailResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ShelterDetailApi {
    @FormUrlEncoded
    @POST("NewsfeedAPI/getsingleshelter")
    suspend fun getShelterDetail(
        @Field("shelter_id") shelter_id: String
    ): Response<ShelterDetailResponse>
}
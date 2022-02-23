package com.aks.doggydoo.adoption.datasource.repo

import com.aks.doggydoo.adoption.datasource.model.AdoptionListResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AdoptionApiService {
    @FormUrlEncoded
    @POST("AdoptionApi/adoptionHome")
    suspend fun getAdoptionList(
        @Field("user_id") user_id:String,
        @Field("latitude") latitude:String,
        @Field("longitude") longitude:String
    ): Response<AdoptionListResponse>
}
package com.bnb.doggydoo.adoption.datasource.repo

import com.bnb.doggydoo.adoption.datasource.model.AdoptionListAllResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AdoptionViewAllApiService {
    @FormUrlEncoded
    @POST("AdoptionApi/postadoptionlist")
    suspend fun getAllAdoptionList(
        @Field("user_id") user_id:String
    ): Response<AdoptionListAllResponse>
}
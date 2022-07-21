package com.bnb.doggydoo.rateplace.datasource.repo

import com.bnb.doggydoo.rateplace.datasource.model.RateResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RateApiService {
    @FormUrlEncoded
    @POST("HomeApi/rateinsert")
    suspend fun rate( @Field("user_id") user_id: String,
                      @Field("park_id") park_id: String,
                      @Field("rate") rate: String,
                      @Field("review") review: String,
                      @Field("pet_friendly") pet_friendly: String
    ): Response<RateResponse>
}
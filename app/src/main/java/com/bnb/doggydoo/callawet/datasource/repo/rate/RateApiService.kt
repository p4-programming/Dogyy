package com.bnb.doggydoo.callawet.datasource.repo.rate

import com.bnb.doggydoo.callawet.datasource.model.rate.RateDoctorResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RateApiService {
    @FormUrlEncoded
    @POST("VatApi/Ratetocallavet")
    suspend fun rate( @Field("callid") user_id: String,
                      @Field("rate") park_id: String,
                      @Field("feedback") rate: String
    ): Response<RateDoctorResponse>
}
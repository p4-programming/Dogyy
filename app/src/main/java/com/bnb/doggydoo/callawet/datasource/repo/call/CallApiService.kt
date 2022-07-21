package com.bnb.doggydoo.callawet.datasource.repo.call

import com.bnb.doggydoo.callawet.datasource.model.call.CallApiResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CallApiService {
    @FormUrlEncoded
    @POST("VatApi/callAdoctor")
    suspend fun call(@Field("user_id") user_id: String,
                     @Field("doctor_id") doctor_id: String,
                     @Field("reason") reason: String,
                     @Field("type") type: String
    ): Response<CallApiResponse>
}
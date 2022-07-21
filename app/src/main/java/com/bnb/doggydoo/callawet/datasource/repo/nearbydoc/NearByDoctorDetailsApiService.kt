package com.bnb.doggydoo.callawet.datasource.repo.nearbydoc

import com.bnb.doggydoo.callawet.datasource.model.nearbydoc.NearByDoctorDetailResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NearByDoctorDetailsApiService {
    @FormUrlEncoded
    @POST("VatApi/Vetdetail")
    suspend fun getDoctorDetails(
        @Field("vetid") vetid: String,
        @Field("user_id") user_id: String
    ): Response<NearByDoctorDetailResponse>
}
package com.aks.doggydoo.callawet.datasource.repo.nearbyhos

import com.aks.doggydoo.callawet.datasource.model.nearbyhos.NearByHospitalDetailResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NearByHospitalApiService {
    @FormUrlEncoded
    @POST("VatApi/clinicDetail")
    suspend fun getNearbyHospitalDetails(
        @Field("clinic_id") clinic_id: String,
        @Field("userid") userid: String
    ): Response<NearByHospitalDetailResponse>
}
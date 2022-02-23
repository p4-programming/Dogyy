package com.aks.doggydoo.callawet.datasource.repo.home

import com.aks.doggydoo.callawet.datasource.model.home.CallawetHomeResponse
import com.aks.doggydoo.callawet.datasource.model.home.ViewAllClinicResponse
import com.aks.doggydoo.callawet.datasource.model.home.ViewAllDoctorResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CallawetHomeApiService {
    @FormUrlEncoded
    @POST("VatApi/vatHomepage")
    suspend fun getCallawetHomeDataList(
        @Field("userid") userid: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<CallawetHomeResponse>

    @FormUrlEncoded
    @POST("VatApi/viewallclinic")
    suspend fun getAllClinic(
        @Field("userid") userid: String,
    ): Response<ViewAllClinicResponse>

    @FormUrlEncoded
    @POST("VatApi/viewalldoctor")
    suspend fun getAllDoctor(
        @Field("userid") userid: String,
    ): Response<ViewAllDoctorResponse>
}
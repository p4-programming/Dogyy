package com.aks.doggydoo.parkdescription.datasource.repo

import com.aks.doggydoo.parkdescription.datasource.model.CheckInChechoutResponse
import com.aks.doggydoo.parkdescription.datasource.model.GetRateListResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PetDescriptionApiService {
    @FormUrlEncoded
    @POST("HomeApi/getParkdetail")
    suspend fun getParkDescription(
        @Field("parkId") park_id: String,
        @Field("user_id") userid: String
    ): Response<GetRateListResponse>

    @FormUrlEncoded
    @POST("AuthApi/parkcheckinandcheckout")
    suspend fun checkinCheckout(
        @Field("type") type: String,
        @Field("parkid") park_id: String,
        @Field("userid") userid: String
    ): Response<CheckInChechoutResponse>
}
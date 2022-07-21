package com.bnb.doggydoo.fostering.datasource.repo

import com.bnb.doggydoo.fostering.datasource.model.FosteringDetailResponse
import com.bnb.doggydoo.fostering.datasource.model.FosteringReqResponse
import com.bnb.doggydoo.fostering.datasource.model.FosteringViewAllResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface fosteringDetailApiService {
    @FormUrlEncoded
    @POST("ForestingApi/fosteringdetail")
    suspend fun getFosterHomeDetailList(
        @Field("fosterid") fosterid: String,
        @Field("userid") userid: String
    ): Response<FosteringDetailResponse>

    @FormUrlEncoded
    @POST("ForestingApi/fosteringsend")
    suspend fun getFosterReqList(
        @Field("type") type: String,
        @Field("user_id") user_id: String,
        @Field("recieve_id") recieve_id: String,
        @Field("fost_pet_id") fost_pet_id: String
    ): Response<FosteringReqResponse>

    @FormUrlEncoded
    @POST("ForestingApi/ViewAll")
    suspend fun getAllFosterDetailList(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<FosteringViewAllResponse>
}
package com.aks.doggydoo.training.datasource.repo

import com.aks.doggydoo.training.datasource.model.SubmitTrainingVideoResponse
import com.aks.doggydoo.training.datasource.model.TrainingDetailResponse
import com.aks.doggydoo.training.datasource.model.TrainingListReposne
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TrainingApiService {
    @POST("NewsfeedAPI/traininglist")
    suspend fun getTrainingList(
    ): Response<TrainingListReposne>

    @FormUrlEncoded
    @POST("NewsfeedAPI/trainingdetail")
    suspend fun getDetail(
        @Field("train_id") id:String
    ):Response<TrainingDetailResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/inserttraining")
    suspend fun uploadTrainingVideo(
        @Field("user_id") user_id:String,
        @Field("title") title:String,
        @Field("url") url:String
    ):Response<SubmitTrainingVideoResponse>
}
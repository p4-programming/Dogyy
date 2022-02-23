package com.aks.doggydoo.articledescription.datasource.repo

import com.aks.doggydoo.articledescription.datasource.model.CommentCountResponse
import com.aks.doggydoo.articledescription.datasource.model.NewsFeedCommentResponse
import com.aks.doggydoo.rateplace.datasource.model.RateResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ArticleDescriptionApiService {
    @FormUrlEncoded
    @POST("NewsfeedAPI/getnewsfeedcomment")
    suspend fun getComment(
        @Field("newsfeed_id") newsfeed_id: String
    ): Response<NewsFeedCommentResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/insertnewsfeedcomment")
    suspend fun postComment(
        @Field("newsfeed_id") newsfeed_id: String,
        @Field("comment") comment: String,
        @Field("user_id") user_id: String
    ): Response<RateResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/getnewsfeedcomment")
    suspend fun getCommentCount(
        @Field("newsfeed_id") newsfeed_id: String
    ): Response<CommentCountResponse>
}
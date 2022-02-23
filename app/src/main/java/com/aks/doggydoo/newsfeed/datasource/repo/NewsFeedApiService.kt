package com.aks.doggydoo.newsfeed.datasource.repo

import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedByIdResponse
import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedCommentResponse
import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedLikeCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NewsFeedApiService {
    @FormUrlEncoded
    @POST("NewsfeedAPI/getallnewsfeedlist")
    suspend fun getNewsFeedById(
        @Field("searchtype") searchtype: String,
        @Field("userid") userid: String,
        @Field("filter") filter: String
    ): Response<NewsFeedByIdResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/newsfeedlistbyid")
    suspend fun getAllNewsFeedList(
        @Field("searchtype") searchtype: String,
    ): Response<NewsFeedByIdResponse>

    @Multipart
    @POST("NewsfeedAPI/insertnewsfeed")
    suspend fun uploadNewsFeed(
        @Part("user_id") user_id: RequestBody,
        @Part("caption") caption: RequestBody,
        @Part("article") article: RequestBody,
        @Part("filetype") filetype: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("news_type") news_type: RequestBody
    ): Response<NewsFeedByIdResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/newsfeedblock")
    suspend fun blockUser(
        @Field("user_id") user_id: String,
        @Field("block_id") block_id: String
    ): Response<NewsFeedByIdResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/insertnewsfeedcomment")
    suspend fun commentPost(
        @Field("newsfeed_id") newsfeed_id: String,
        @Field("comment") comment: String,
        @Field("user_id") user_id: String
    ): Response<NewsFeedLikeCommentResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/postlikenewsfeed")
    suspend fun likePost(
        @Field("newsfeed_id") newsfeed_id: String,
        @Field("like") like: String,
        @Field("user_id") user_id: String
    ): Response<NewsFeedLikeCommentResponse>


    @FormUrlEncoded
    @POST("NewsfeedAPI/getnewsfeedcomment")
    suspend fun getCommentListPost(
        @Field("newsfeed_id") newsfeed_id: String,
    ): Response<NewsFeedCommentResponse>

    @FormUrlEncoded
    @POST("NewsfeedAPI/newsfeeddelete")
    suspend fun deletePost(
        @Field("user_id") user_id: String,
        @Field("newsfeed_id") newsfeed_id: String,
    ): Response<NewsFeedLikeCommentResponse>
}
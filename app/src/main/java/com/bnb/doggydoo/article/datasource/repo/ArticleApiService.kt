package com.bnb.doggydoo.article.datasource.repo

import com.bnb.doggydoo.article.datasource.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ArticleApiService {
    @Multipart
    @POST("NotificationApi/insertblog")
    suspend fun insertArticle(
        @Part("user_id") user_id: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
        @Part("article") article: RequestBody,
        @Part("news_type") news_type: RequestBody
    ): Response<InsertArticleResponse>

    @FormUrlEncoded
    @POST("NotificationApi/bloghome")
    suspend fun getAricleDataHome(
        @Field("user_id") user_id: String
    ): Response<ArticleHomeResponse>


    @FormUrlEncoded
    @POST("NotificationApi/ViewAllBlog")
    suspend fun getAricleViewAll(
        @Field("user_id") user_id: String,
        @Field("type") type: String
    ): Response<AllArticleResponse>

    @FormUrlEncoded
    @POST("NotificationApi/getblogdetail")
    suspend fun getSingleAricleDetail(
        @Field("type") type: String,
        @Field("blog_id") blog_id: String,
        @Field("user_id") user_id: String
    ): Response<SingleArticleResponse>


    @FormUrlEncoded
    @POST("NotificationApi/postlikeblog")
    suspend fun getAricleLikeDetail(
        @Field("type") type: String,
        @Field("blog_id") blog_id: String,
        @Field("user_id") user_id: String,
        @Field("like") like: String
    ): Response<InsertArticleResponse>

    @FormUrlEncoded
    @POST("NotificationApi/postcommentblog")
    suspend fun getAricleCommentDetail(
        @Field("type") type: String,
        @Field("blog_id") blog_id: String,
        @Field("user_id") user_id: String,
        @Field("comment") comment: String
    ): Response<InsertArticleResponse>


    @FormUrlEncoded
    @POST("NotificationApi/getblogcomment")
    suspend fun getAricleComment(
        @Field("blog_id") blog_id: String,
    ): Response<ArticleCommentListResponse>

}
package com.bnb.doggydoo.myprofile.datasource.repo

import com.bnb.doggydoo.myprofile.datasource.model.UserImageUploadResponse
import com.bnb.doggydoo.myprofile.datasource.model.pet.MyPetResponse
import com.bnb.doggydoo.myprofile.datasource.model.profile.EditProfileResponse
import com.bnb.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.bnb.doggydoo.myprofile.datasource.model.profile.UpdateStatusResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MyProfileApiService {
    /**
     * update user Image
     */
    @Multipart
    @POST("AuthApi/userProfileupdate")
    suspend fun userProfileUpdate(
        @Part("user_id") user_id: RequestBody,
        @Part user_profile: MultipartBody.Part
    ): Response<UserImageUploadResponse>

    /**
     * get my pet
     */
    @FormUrlEncoded
    @POST("AuthApi/getMypet")
    suspend fun getMyPet(
        @Field("userid") user_id: String
    ): Response<MyPetResponse>

    /**
     * get user info
     */
    @FormUrlEncoded
    @POST("AuthApi/getUserhome")
    suspend fun getUserprofile(
        @Field("user_id") user_id: String,
        @Field("viewer_id") viewer_id: String
    ): Response<MyProfileResponse>


    @Multipart
    @POST("AuthApi/userEditProfile")
    suspend fun editUserProfile(
        @Part("user_id") user_id: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("user_decription") userDescription: RequestBody,
        @Part("usernname") username: RequestBody,
        @Part("DOB") dob: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("visibility") visibility: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part user_profile: MultipartBody.Part
    ): Response<EditProfileResponse>

    /**
     * get user info
     */
    @FormUrlEncoded
    @POST("AuthApi/userstatusupdate")
    suspend fun getUpdateUserStatus(
        @Field("user_id") user_id: String,
        @Field("status") status: String,
        @Field("type") type: String
    ): Response<UpdateStatusResponse>


    @FormUrlEncoded
    @POST("HomeApi/freindsrequestsent")
    suspend fun sendFriendReq(
        @Field("sender_id") user_id: String,
        @Field("recieve_id") status: String
    ): Response<UpdateStatusResponse>


    @FormUrlEncoded
    @POST("HomeApi/unfriendandreport")
    suspend fun blockReportUser(
        @Field("Request_id") Request_id: String,
        @Field("type") type: String,
        @Field("user_id") user_id: String,
    ): Response<UpdateStatusResponse>
}
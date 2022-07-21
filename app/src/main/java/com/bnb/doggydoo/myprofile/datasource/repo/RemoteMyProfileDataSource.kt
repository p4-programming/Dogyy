package com.bnb.doggydoo.myprofile.datasource.repo

import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteMyProfileDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch UserProfileUpdate Response from network
     * */
    suspend fun fetchUserProfileUpdateResponse(
        userId: String,
        image: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .userProfileUpdate(MultipartFile.createPartFromString(userId), user_profile = image)
        }

    /**
     * fetch MyPets Response from network
     * */
    suspend fun fetchMyPetsResponse(userid: String) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .getMyPet(user_id = userid)
        }

    /**
     * fetch MyProfileInfo Response from network
     * */
    suspend fun fetchMyProfileInfoResponse(userid: String,viewer_id: String) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .getUserprofile(userid,viewer_id)
        }

    /**
     * fetch MyEditProfileInfo Response from network
     * */
    suspend fun fetchEditProfileInfoResponse(
        user_id: String,
        user_name: String,
        age: String,
        user_decription: String,
        username: String,
        DOB: String,
        email: String,
        gender: String,
        visibility: String,
        latitude:String,
        longitude: String,
        profile: MultipartBody.Part
    ) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .editUserProfile(
                    MultipartFile.createPartFromString(user_id),
                    MultipartFile.createPartFromString(user_name),
                    MultipartFile.createPartFromString(age),
                    MultipartFile.createPartFromString(user_decription),
                    MultipartFile.createPartFromString(username),
                    MultipartFile.createPartFromString(DOB),
                    MultipartFile.createPartFromString(email),
                    MultipartFile.createPartFromString(gender),
                    MultipartFile.createPartFromString(visibility),
                    MultipartFile.createPartFromString(latitude),
                    MultipartFile.createPartFromString(longitude),
                    profile
                )
        }

    /**
     * fetch MyProfileInfo Response from network
     * */
    suspend fun fetchUpdateInfoResponse(
        user_id: String,
        status: String,
        type: String
     ) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .getUpdateUserStatus(
                    user_id = user_id,
                    status = status,
                    type = type

                )
        }


    suspend fun fetchFriendReqInfoResponse(
        sender_id: String,
        recieve_id: String
    ) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .sendFriendReq(
                    sender_id,recieve_id

                )
        }

    suspend fun blockReportUserInfoResponse(
        Request_id: String,
        type: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(MyProfileApiService::class.java)
                .blockReportUser(
                    Request_id,type,user_id
                )
        }
}
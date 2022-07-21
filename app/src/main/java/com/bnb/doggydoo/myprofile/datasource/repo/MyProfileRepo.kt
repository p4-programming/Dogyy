package com.bnb.doggydoo.myprofile.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyProfileRepo @Inject
constructor(
    private var remoteMyProfileDataSource: RemoteMyProfileDataSource
) {
    fun updateUserProfileImageLiveData(userId: String, image: MultipartBody.Part) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.fetchUserProfileUpdateResponse(userId, image) }
    )

    fun updateMyPetLiveData(userid: String) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.fetchMyPetsResponse(userid = userid) }
    )

    fun updateMyProfileInfoLiveData(userid: String,viewer_id:String) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.fetchMyProfileInfoResponse(userid,viewer_id) }
    )

    fun editUserLiveData(
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
    ) = resultLiveData(
        networkCall = {
            remoteMyProfileDataSource.fetchEditProfileInfoResponse(
                user_id,
                user_name,
                age,
                user_decription,
                username,
                DOB,
                email,
                gender,
                visibility,
                latitude,
                longitude,
                profile
            )
        }
    )

    fun updateStatusInfoLiveData(
        user_id: String,
        status: String,
        type: String
    ) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.fetchUpdateInfoResponse(
            user_id = user_id,
            status = status,
            type = type
        )
        }
    )


    fun friendReqInfoLiveData(
        sender_id: String,
        recieve_id: String
    ) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.fetchFriendReqInfoResponse(
            sender_id,recieve_id
        )
        }
    )

    fun blockReportInfoLiveData(
        Request_id: String,
        type: String,
        user_id: String
    ) = resultLiveData(
        networkCall = { remoteMyProfileDataSource.blockReportUserInfoResponse(
            Request_id,type,user_id
        )
        }
    )

}
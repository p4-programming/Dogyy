package com.bnb.doggydoo.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.myprofile.datasource.repo.MyProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(var myProfileRepo: MyProfileRepo) : ViewModel() {

    fun getUploadProfileImageData(userId: String, image: MultipartBody.Part) =
        myProfileRepo.updateUserProfileImageLiveData(userId, image)

    fun getMyPetData(userId: String) =
        myProfileRepo.updateMyPetLiveData(userId)

    fun getMyProfileInfoData(userId: String,viewer_id:String) =
        myProfileRepo.updateMyProfileInfoLiveData(userId,viewer_id)

    fun getEditUserData(
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
        myProfileRepo.editUserLiveData(
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

    fun getUserUpdateInfoData(
        user_id: String,
        status: String,
        type: String
    ) =
        myProfileRepo.updateStatusInfoLiveData(user_id,status,type)

    fun friendReqInfoData(
        sender_id: String,
        recieve_id: String
    ) =
        myProfileRepo.friendReqInfoLiveData(sender_id,recieve_id)

    fun blockReportUserData(
        Request_id: String,
        type: String,
        user_id: String
    ) =
        myProfileRepo.blockReportInfoLiveData(Request_id,type,user_id)
}
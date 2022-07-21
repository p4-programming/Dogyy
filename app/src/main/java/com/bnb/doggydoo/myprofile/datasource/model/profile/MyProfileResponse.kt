package com.bnb.doggydoo.myprofile.datasource.model.profile

import com.bnb.doggydoo.myprofile.datasource.model.pet.MyPetDetail
import com.google.gson.annotations.SerializedName

data class MyProfileResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("check_friend")
    var check_friend: String,
    @SerializedName("friend_count")
    var friend_count: String,
    @SerializedName("requestid")
    var requestid: String,
    @SerializedName("userdetails")
    var userdetails: List<UserDetail>,
    @SerializedName("mypet")
    var mypet: List<MyPetDetail>,
    @SerializedName("newupload")
    var newupload: List<NewUploadData>
)
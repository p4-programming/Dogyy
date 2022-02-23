package com.aks.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class FriendReqList(
    @SerializedName("type")
    var type: String,
    @SerializedName("Request_id")
    var Request_id: String,
    @SerializedName("Sender_id")
    var Sender_id: String,
    @SerializedName("Sender_Name")
    var Sender_Name: String,
    @SerializedName("Sender_photo")
    var Sender_photo: String,
    @SerializedName("Reciever_id")
    var Reciever_id: String,
    @SerializedName("reciever_name")
    var reciever_name: String,
    @SerializedName("Reciever_photo")
    var Reciever_photo: String
)

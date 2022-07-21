package com.bnb.doggydoo.chatMessage.datasource.model.request

import com.google.gson.annotations.SerializedName

data class ReceiveRequest(
    @SerializedName("type")
    var type: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("send_date")
    var send_date: String,
    @SerializedName("user_name")
    var user_name: String,
    @SerializedName("day")
    var day: String,
    @SerializedName("user_image")
    var user_image: String,
    @SerializedName("pet_name")
    var pet_name: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("pet_id")
    var pet_id: String,
    @SerializedName("userUid")
    var userUid: String
)
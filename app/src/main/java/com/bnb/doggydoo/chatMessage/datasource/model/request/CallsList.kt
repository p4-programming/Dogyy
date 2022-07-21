package com.bnb.doggydoo.chatMessage.datasource.model.request

import com.google.gson.annotations.SerializedName

data class CallsList(
    @SerializedName("id")
    var id: String,
    @SerializedName("call_type")
    var call_type: String,
    @SerializedName("message_type")
    var message_type: String,
    @SerializedName("date_time")
    var date_time: String,
    @SerializedName("User_id")
    var User_id: String,
    @SerializedName("User_name")
    var User_name: String,
    @SerializedName("profile_pic")
    var profile_pic: String

)

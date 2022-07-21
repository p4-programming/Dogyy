package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class UserReminderList(
    @SerializedName("reminder_id")
    var reminder_id: String,
    @SerializedName("Type")
    var Type: String,
    @SerializedName("create")
    var create: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("Pet_name")
    var Pet_name: String
)

package com.bnb.doggydoo.homemodule.datasource.repo.home

import com.bnb.doggydoo.homemodule.datasource.model.home.UserLostFoundList
import com.bnb.doggydoo.homemodule.datasource.model.home.UserPetPlayList
import com.bnb.doggydoo.homemodule.datasource.model.home.UserReminderList
import com.google.gson.annotations.SerializedName

data class UserUpcomingResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("reminder")
    var userReminderList: List<UserReminderList>,
    @SerializedName("lostandfound")
    var userLostandFoundList: List<UserLostFoundList>,
    @SerializedName("petpaylist")
    var userPetpayList: List<UserPetPlayList>
)

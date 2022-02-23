package com.aks.doggydoo.playdate.datasource.model.homepage

import com.google.gson.annotations.SerializedName

data class PlayDateHomeResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("nearbypets")
    var nearbypets: List<NearbyPlayDates>,
    @SerializedName("upcoming")
    var upcomingList: List<UpcomingPlayDates>,
    @SerializedName("invites")
    var invitesList: List<InvitesPlayDates>,
    @SerializedName("lastdate")
    var lastPlayDateList: List<LastPlayDates>,
    @SerializedName("suitable_mate")
    var suitableMateList: List<NearbyPlayDates>
)
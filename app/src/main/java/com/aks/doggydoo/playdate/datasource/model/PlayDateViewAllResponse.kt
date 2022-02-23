package com.aks.doggydoo.playdate.datasource.model

import com.aks.doggydoo.playdate.datasource.model.homepage.LastPlayDates
import com.google.gson.annotations.SerializedName

data class PlayDateViewAllResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("viewallpetplay")
    var nearbypets: List<LastPlayDates>
)

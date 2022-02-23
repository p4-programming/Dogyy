package com.aks.doggydoo.playdate.datasource.model

import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.Petdetail
import com.google.gson.annotations.SerializedName

class PlayDateDetailResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("playcount")
    var playcount: Int,
    @SerializedName("petdetail")
    var petdetail: List<Petdetail>,
    @SerializedName("playdate")
    var playdate: List<PlayDate>
)
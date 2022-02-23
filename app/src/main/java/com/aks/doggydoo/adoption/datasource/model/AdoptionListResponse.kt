package com.aks.doggydoo.adoption.datasource.model

import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedDetail
import com.google.gson.annotations.SerializedName

data class AdoptionListResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("adoption_list")
    var adoptionlist: List<AdoptionListData> ,
    @SerializedName("shelter_list")
    var shelterList: List<ShelterList>
)
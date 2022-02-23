package com.aks.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class FosteringHomeResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("nearbypets")
    var nearbypetslist: List<NearByFosterPetList>,
    @SerializedName("fosteringinvites")
    var fosteringinvitesList: List<NearByFosterPetList>,
    @SerializedName("Recentltforseting")
    var Recentltforsetinglist: List<NearByFosterPetList>,
    @SerializedName("FosterHero")
    var heroforsetinglist: List<NearByFosterHeroList>


)

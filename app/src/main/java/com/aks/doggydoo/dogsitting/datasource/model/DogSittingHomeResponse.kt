package com.aks.doggydoo.dogsitting.datasource.model

import com.google.gson.annotations.SerializedName

data class DogSittingHomeResponse(
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("nearbypets")
    var nearbydogsitpetlist: List<NearByDogsitPetList>,
    @SerializedName("invites")
    var dogsittinginvitesList: List<NearByDogsitPetList>,
    @SerializedName("herosnearby")
    var herosnearbydogsitlist: List<DogSitHerosList>,
    @SerializedName("recent")
    var recentdogsitlist: List<NearByDogsitPetList>
)

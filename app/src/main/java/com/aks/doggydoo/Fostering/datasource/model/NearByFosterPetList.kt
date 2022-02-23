package com.aks.doggydoo.fostering.datasource.model

import com.google.gson.annotations.SerializedName

data class NearByFosterPetList(
    @SerializedName("foster_id")
    var foster_id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("age")
    var age: String,
    @SerializedName("age_type")
    var age_type: String,
    @SerializedName("pic")
    var pic: String,
    @SerializedName("breed")
    var breed: String,
    @SerializedName("lattitue")
    var lattitue: String,
    @SerializedName("longitute")
    var longitute: String
)

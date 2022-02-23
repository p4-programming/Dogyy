package com.aks.doggydoo.adoptdogdetails.datasource.model

import com.google.gson.annotations.SerializedName

class AdoptionDogList(
    @SerializedName("name")
    var name: String,
    @SerializedName("pet_id")
    var pet_id: String,
    @SerializedName("age")
    var age: String,
    @SerializedName("pet_age_month")
    var pet_age_month: String,
    @SerializedName("age_type")
    var age_type: String,
    @SerializedName("pic")
    var pic: List<String>,
    @SerializedName("breed")
    var breed: String?,
    @SerializedName("description")
    var description: String,
    @SerializedName("User")
    var user: UserOfAdoptionDog

)

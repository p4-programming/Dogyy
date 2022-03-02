package com.aks.doggydoo.adoption.datasource.model

import com.google.gson.annotations.SerializedName

data class Singlesheltetlist(

    @SerializedName("Name")
    var Name: String,

    @SerializedName("Pet_List")
    var Pet_List: List<Pet>,

    @SerializedName("address")
    var address: String,

    @SerializedName("city")
    var city: String,

    @SerializedName("country")
    var country: String,

    @SerializedName("lattitue")
    var lattitue: String,

    @SerializedName("longitute")
    var longitute: String,

    @SerializedName("mobile")
    var mobile: String,

    @SerializedName("photo")
    var photo: String,

    @SerializedName("state")
    var state: String,

    @SerializedName("pincode")
    var pincode: String,


)
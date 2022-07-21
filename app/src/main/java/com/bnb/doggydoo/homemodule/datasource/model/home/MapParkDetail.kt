package com.bnb.doggydoo.homemodule.datasource.model.home

import com.google.gson.annotations.SerializedName

data class MapParkDetail(
    @SerializedName("type")
    var type: String,
    @SerializedName("response_type")
    var response_type: String,
    @SerializedName("id")
    var parkid: String,
    @SerializedName("name")
    var park_name: String,
    @SerializedName("image")
    var park_image: String,
    @SerializedName("lattitute")
    var park_lattitute: String,
    @SerializedName("longitute")
    var park_longitute: String,
    @SerializedName("km")
    var km: String,
)

package com.bnb.doggydoo.callawet.datasource.model.home

import com.google.gson.annotations.SerializedName

data class NearByDocList(
    @SerializedName("id")
    var id: String,
    @SerializedName("vat_name")
    var vat_name: String,
    @SerializedName("vat_image")
    var vat_image: String,
    @SerializedName("vatspecial")
    var vatspecial: String
)

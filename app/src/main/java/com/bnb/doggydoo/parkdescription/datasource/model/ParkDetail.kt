package com.bnb.doggydoo.parkdescription.datasource.model

import com.bnb.doggydoo.homemodule.datasource.model.home.ParkImageList
import com.google.gson.annotations.SerializedName

data class ParkDetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("place_name")
    var place_name: String,
    @SerializedName("place_type")
    var place_type: String,
    @SerializedName("zone")
    var zone: String,
    @SerializedName("place_email")
    var place_email: String,
    @SerializedName("place_phone")
    var place_phone: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("state")
    var state: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("vat_latitude")
    var vat_latitude: String,
    @SerializedName("vat_longitude")
    var vat_longitude: String,
    @SerializedName("open_time")
    var open_time: String,
    @SerializedName("close_time")
    var close_time: String,
    @SerializedName("place_description")
    var place_description: String,
    @SerializedName("place_image")
    var parkImageList: List<ParkImageList>,
    @SerializedName("status")
    var status: String,
    @SerializedName("deleted")
    var deleted: String,
    @SerializedName("total_park_user_count")
    var total_park_user_count: String,
    @SerializedName("park_close_open")
    var park_close_open: String,
    @SerializedName("checkin_status")
    var checkin_status: String

)

package com.aks.doggydoo.callawet.datasource.model.nearbydoc

import com.google.gson.annotations.SerializedName

data class NearByDoctorDetailList(
    @SerializedName("id")
    var id: String,
    @SerializedName("km")
    var km: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("Degree")
    var Degree: String,
    @SerializedName("open_time")
    var open_time: String,
    @SerializedName("close_time")
    var close_time: String,
    @SerializedName("Description")
    var Description: String,
    @SerializedName("Vet_image")
    var Vet_image: String,
    @SerializedName("specialization")
    var specialization: String,
    @SerializedName("Clinic_name")
    var Clinic_name: String,
    @SerializedName("Clinic_id")
    var Clinic_id: String,
    @SerializedName("Clinic_image")
    var Clinic_image: String
)

package com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel

import com.google.gson.annotations.SerializedName

data class PetDescriptionResponse (
    @SerializedName("responseCode")
    var responseCode: String,
    @SerializedName("responseMessage")
    var responseMessage: String,
    @SerializedName("petdetail")
    var petdetail: Petdetail,
    @SerializedName("documentdetail")
    var documentDetail: List<Documentdetail>,
    @SerializedName("Petreminder")
    var petReminder: List<Petreminder>,
    @SerializedName("PetImage")
    var petImage: List<String>,
    @SerializedName("Userdetail")
    var userdetail: UserDetail,


)
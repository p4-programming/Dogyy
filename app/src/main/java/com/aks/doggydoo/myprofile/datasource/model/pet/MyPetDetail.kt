package com.aks.doggydoo.myprofile.datasource.model.pet
import com.google.gson.annotations.SerializedName
data class MyPetDetail(
    @SerializedName("id") val id: String,
    @SerializedName("pet_name") val pet_name: String,
    @SerializedName("pet_age") val pet_age: String,
    @SerializedName("pet_age_type") val pet_age_type: String,
    @SerializedName("pet_gender") val pet_gender: String,
    @SerializedName("pet_weight") val pet_weight: String,
    @SerializedName("pet_weight_type") val pet_weight_type: String,
    @SerializedName("pet_description") val pet_description: String,
    @SerializedName("living") val living: String,
    @SerializedName("pet_medical_conditions") val pet_medical_conditions: String,
    @SerializedName("is_pet_vaccinated") val is_pet_vaccinated: String,
    @SerializedName("pet_image") val pet_image: String,
    @SerializedName("breed") val breed: String
)
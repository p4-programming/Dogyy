package com.bnb.doggydoo.adoption.datasource.model

data class ShelterDetailDogViewAllResponse(
    val Pet_List: List<Pet>,
    val responseCode: String,
    val responseMessage: String
)
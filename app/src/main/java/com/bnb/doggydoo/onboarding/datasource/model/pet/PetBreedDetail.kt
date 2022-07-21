package com.bnb.doggydoo.onboarding.datasource.model.pet

import com.google.gson.annotations.SerializedName

class PetBreedDetail(
    @SerializedName("id")
    var id: String,
    @SerializedName("category")
    var category: String,
)
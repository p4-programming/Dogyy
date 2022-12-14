package com.bnb.doggydoo.adoption.datasource.repo

import com.bnb.doggydoo.adoption.datasource.model.ShelterDetailDogViewAllResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ViewAllShelterDogAPI {

    @FormUrlEncoded
    @POST("NewsfeedAPI/viewallshelterpet")
    suspend fun getSheltersDog(
        @Field("shelter_id") shelter_id: String
    ): Response<ShelterDetailDogViewAllResponse>
}
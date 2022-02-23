package com.aks.doggydoo.postadoptiondetail.datasource.repo

import com.aks.doggydoo.postadoptiondetail.datasource.model.PostAdoptionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostAdoptionApiService {
    @Multipart
    @POST("AdoptionApi/postadoption")
    suspend fun postAdoption(
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("age_type") age_type: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("about_dog") about_dog: RequestBody,
        @Part("vaccinated") vaccinated: RequestBody,
        @Part("medical_condition") medical_condition: RequestBody,
        @Part pet_pic: List<MultipartBody.Part>,
        @Part("user_id") user_id: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("user_email") user_email: RequestBody,
        @Part("user_mobile") user_mobile: RequestBody,
        @Part("living_type") living_type: RequestBody,
        @Part("shelter_id") shelter_id: RequestBody,
        @Part user_photo_id: List<MultipartBody.Part>
    ): Response<PostAdoptionResponse>
}
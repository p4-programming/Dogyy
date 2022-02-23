package com.aks.doggydoo.rateplace.datasource.repo

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteRatingDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch Rate Response from network
     * */
    suspend fun fetchRateResponse(
        user_id: String,
        park_id: String,
        rate: String,
        review: String,
        pet_friendly: String
    ) =
        getResult {
            apiFactory.createService(RateApiService::class.java)
                .rate(
                    user_id, park_id, rate, review, pet_friendly
                )
        }

}
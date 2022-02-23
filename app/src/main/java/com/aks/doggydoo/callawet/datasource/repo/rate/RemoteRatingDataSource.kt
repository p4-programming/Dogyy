package com.aks.doggydoo.callawet.datasource.repo.rate

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteRatingDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch Rate Response from network
     * */
    suspend fun fetchRateResponse(
        callid: String,
        rate: String,
        feedback: String
    ) =
        getResult {
            apiFactory.createService(RateApiService::class.java)
                .rate(
                    callid, rate,feedback
                )
        }

}
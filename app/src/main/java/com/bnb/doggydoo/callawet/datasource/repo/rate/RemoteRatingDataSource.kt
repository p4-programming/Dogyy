package com.bnb.doggydoo.callawet.datasource.repo.rate

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
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
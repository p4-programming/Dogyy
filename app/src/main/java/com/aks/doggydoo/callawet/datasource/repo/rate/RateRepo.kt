package com.aks.doggydoo.callawet.datasource.repo.rate

import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateRepo @Inject constructor(private var remoteRatingDataSource: RemoteRatingDataSource) {
    fun rateLiveData(  callid: String,
                       rate: String,
                       feedback: String) = resultLiveData(
        networkCall = { remoteRatingDataSource.fetchRateResponse(callid,rate, feedback) }
    )
}
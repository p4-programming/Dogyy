package com.aks.doggydoo.rateplace.datasource.repo

import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateRepo @Inject
constructor(
    private var remoteRatingDataSource: RemoteRatingDataSource
) {
    fun rateLiveData(  user_id: String,
                       park_id: String,
                       rate: String,
                       review: String,
                       pet_friendly: String) = resultLiveData(
        networkCall = { remoteRatingDataSource.fetchRateResponse(user_id, park_id, rate, review, pet_friendly) }
    )
}
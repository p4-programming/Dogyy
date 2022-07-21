package com.bnb.doggydoo.callawet.datasource.repo.callreason

import com.bnb.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallReasonRepo @Inject constructor(private var callaWetDataSource: RemoteCallaWetDataSource) {
    fun callReasonLiveData() = resultLiveData(
        networkCall = { callaWetDataSource.fetchCallReasonResponse() }
    )
}
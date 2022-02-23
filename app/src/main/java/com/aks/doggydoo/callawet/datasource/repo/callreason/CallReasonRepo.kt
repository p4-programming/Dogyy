package com.aks.doggydoo.callawet.datasource.repo.callreason

import com.aks.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallReasonRepo @Inject constructor(private var callaWetDataSource: RemoteCallaWetDataSource) {
    fun callReasonLiveData() = resultLiveData(
        networkCall = { callaWetDataSource.fetchCallReasonResponse() }
    )
}
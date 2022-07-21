package com.bnb.doggydoo.callawet.datasource.repo.call

import com.bnb.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallRepo @Inject constructor(private var remoteCallaWetDataSource: RemoteCallaWetDataSource) {
    fun getCallLiveData(user_id: String,
                        doctor_id: String,
                        reason: String,
                        type: String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchCall(user_id,doctor_id,reason,type)
        }
    )

}
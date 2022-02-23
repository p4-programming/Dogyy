package com.aks.doggydoo.callawet.datasource.repo.nearbyhos

import com.aks.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearByHosDetailRepo @Inject constructor(private var remoteCallaWetDataSource: RemoteCallaWetDataSource) {
    fun getHospitalDetailLiveData(clinic_id:String,user_id: String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchHosDetailResponse(clinic_id,user_id)
        }
    )

}
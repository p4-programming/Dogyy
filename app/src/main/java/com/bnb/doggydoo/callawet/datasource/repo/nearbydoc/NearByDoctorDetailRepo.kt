package com.bnb.doggydoo.callawet.datasource.repo.nearbydoc

import com.bnb.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearByDoctorDetailRepo @Inject constructor(private var remoteCallaWetDataSource: RemoteCallaWetDataSource) {
    fun getDoctorDetailLiveData(vetid:String,user_id: String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchDocDetailResponse(vetid,user_id)
        }
    )

}
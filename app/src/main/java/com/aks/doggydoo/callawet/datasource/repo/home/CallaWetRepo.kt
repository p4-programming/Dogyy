package com.aks.doggydoo.callawet.datasource.repo.home

import com.aks.doggydoo.callawet.datasource.repo.RemoteCallaWetDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallaWetRepo @Inject constructor(private var remoteCallaWetDataSource: RemoteCallaWetDataSource) {
    fun getCallaWetListLiveData(userid: String,latitude:String,
                                longitude:String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchCallaWetResponse(userid,latitude,longitude)
        }
    )


    fun getAllClinicLiveData(userid: String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchAllClinicResponse(userid)
        }
    )


    fun getAllDocLiveData(userid: String) = resultLiveData(
        networkCall = {
            remoteCallaWetDataSource.fetchAllDocResponse(userid)
        }
    )

}
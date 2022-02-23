package com.aks.doggydoo.fostering.datasource.repo

import com.aks.doggydoo.fostering.datasource.RemoteFosteringDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FosteringRepo @Inject constructor(private var remoteFosteringDataSource: RemoteFosteringDataSource) {
    fun getFosteringLiveData(user_id: String,latitude:String,
                             longitude:String) = resultLiveData(
        networkCall = {
            remoteFosteringDataSource.fetchFosteringResponse(user_id,latitude,longitude)
        }
    )

    fun getFosteringLiveDataDetail(fosterid: String,userid: String) = resultLiveData(
        networkCall = {
            remoteFosteringDataSource.fetchFosteringDetailResponse(fosterid,userid)
        }
    )

    fun sentFosteringReqLiveDataDetail(type: String,user_id: String,recieve_id: String, fost_pet_id: String) = resultLiveData(
        networkCall = {
            remoteFosteringDataSource.fetchFosteringReqDetailResponse(type, user_id,recieve_id,fost_pet_id)
        }
    )

    fun getAllFosteringLiveDataDetail(user_id: String,type: String,latitude:String, longitude:String) = resultLiveData(
        networkCall = {
            remoteFosteringDataSource.fetchAllFosteringDetailResponse(user_id,type,latitude,longitude)
        }
    )

}
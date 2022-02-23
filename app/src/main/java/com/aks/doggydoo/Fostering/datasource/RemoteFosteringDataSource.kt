package com.aks.doggydoo.fostering.datasource

import com.aks.doggydoo.fostering.datasource.repo.fosteringDetailApiService
import com.aks.doggydoo.fostering.datasource.repo.fosteringHomeApiService
import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteFosteringDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    suspend fun fetchFosteringResponse(
        user_id: String,
        latitude:String,
        longitude:String
    ) =
        getResult {
            apiFactory.createService(fosteringHomeApiService::class.java)
                .getFosterHomeDataList(
                    user_id,latitude,longitude
                )
        }

    suspend fun fetchFosteringDetailResponse(
        fosterid: String,
        userid: String
    ) =
        getResult {
            apiFactory.createService(fosteringDetailApiService::class.java)
                .getFosterHomeDetailList(
                    fosterid,userid
                )
        }


    suspend fun fetchFosteringReqDetailResponse(
        type: String,user_id: String,recieve_id: String, fost_pet_id: String
    ) =
        getResult {
            apiFactory.createService(fosteringDetailApiService::class.java)
                .getFosterReqList(
                   type, user_id,recieve_id,fost_pet_id
                )
        }



    suspend fun fetchAllFosteringDetailResponse(
        user_id: String,type: String, latitude:String, longitude:String
    ) =
        getResult {
            apiFactory.createService(fosteringDetailApiService::class.java)
                .getAllFosterDetailList(
                    user_id,type,latitude,longitude
                )
        }
}
package com.aks.doggydoo.dogsitting.datasource.repo

import com.aks.doggydoo.dogsitting.datasource.RemoteDogSittingDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogsittingRepo @Inject constructor(private var remoteDogSittingDataSource: RemoteDogSittingDataSource) {
    fun getDogsittingLiveData(user_id: String,latitude:String, longitude:String) = resultLiveData(
        networkCall = {
            remoteDogSittingDataSource.fetchDogSitingHomeResponse(user_id,latitude,longitude)
        }
    )


    fun getAllDogsittingLiveData(user_id: String,type:String,latitude:String, longitude:String) = resultLiveData(
        networkCall = {
            remoteDogSittingDataSource.fetchAllDogSitingResponse(user_id,type, latitude,longitude)
        }
    )


    fun getAllDogsitReqLiveData(user_id: String,recive_id:String, pet_id:String, dogsitdate:String,dogsittime:String) = resultLiveData(
        networkCall = {
            remoteDogSittingDataSource.fetchDogSitReqResponse(user_id,recive_id, pet_id,dogsitdate,dogsittime)
        }
    )

    fun getAllDogLiveData(user_id: String) = resultLiveData(
        networkCall = {
            remoteDogSittingDataSource.fetchAllDogResponse(user_id)
        }
    )

    fun getRateHeroLiveData(userid: String, heroid:String,rate:String,feedback:String) = resultLiveData(
        networkCall = {
            remoteDogSittingDataSource.fetchHeroRateResponse(userid,heroid,rate,feedback)
        }
    )

}
package com.bnb.doggydoo.dogsitting.datasource

import com.bnb.doggydoo.dogsitting.datasource.repo.DogSittingHomeApiService
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteDogSittingDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    suspend fun fetchDogSitingHomeResponse(
        user_id: String, latitude:String, longitude:String
    ) =
        getResult {
            apiFactory.createService(DogSittingHomeApiService::class.java)
                .getDogsitHomeDataList(
                    user_id, latitude, longitude
                )
        }


    suspend fun fetchAllDogSitingResponse(
        user_id: String,type:String, latitude:String, longitude:String
    ) =
        getResult {
            apiFactory.createService(DogSittingHomeApiService::class.java)
                .getDogsitAllList(
                    user_id,type, latitude, longitude
                )
        }


    suspend fun fetchDogSitReqResponse(
        user_id: String,recive_id:String, pet_id:String, dogsitdate:String,dogsittime:String
    ) =
        getResult {
            apiFactory.createService(DogSittingHomeApiService::class.java)
                .sentDogsitRequest(
                    user_id,recive_id, pet_id, dogsitdate,dogsittime
                )
        }

    suspend fun fetchAllDogResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(DogSittingHomeApiService::class.java)
                .getDog(
                    user_id
                )
        }

    suspend fun fetchHeroRateResponse(
        userid: String, heroid:String,rate:String,feedback:String
    ) =
        getResult {
            apiFactory.createService(DogSittingHomeApiService::class.java)
                .rateHeroUser(
                    userid,heroid,rate,feedback
                )
        }
}
package com.bnb.doggydoo.parkdescription.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetDescriptionRepo @Inject
constructor(
    private var remotePetDescriptionDataSource: RemotePetDescriptionDataSource
) {
    fun getParkDescriptionLiveData(
        park_id: String, user_id: String
    ) = resultLiveData(
        networkCall = { remotePetDescriptionDataSource.fetchParkDescriptionResponse(park_id,user_id) }
    )


    fun getCheckInLiveData(
        type: String, park_id: String, userid:String
    ) = resultLiveData(
        networkCall = { remotePetDescriptionDataSource.fetchCheckInResponse(type, park_id, userid) }
    )
}
package com.bnb.doggydoo.adoption.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdoptionRepo @Inject constructor(
    private var remoteAdoptionDataSource: RemoteAdoptionDataSource
) {
    fun getAdoptionListLiveData(
        user_id: String,
        latitude: String,
        longitude: String
    ) = resultLiveData(
        networkCall = {
            remoteAdoptionDataSource.fetchAdoptionResponse(user_id, latitude, longitude)
        }
    )

    fun getAllAdoptionListLiveData(user_id: String) = resultLiveData(
        networkCall = {
            remoteAdoptionDataSource.fetchAllAdoptionResponse(user_id)
        }
    )

    fun getAllShelterListLiveData(user_id: String) = resultLiveData(
        networkCall = {
            remoteAdoptionDataSource.fetchAllShelterResponse(user_id)
        }
    )

    fun getAllShelterDetailLiveData(shelter_id: String) = resultLiveData(
        networkCall = {
            remoteAdoptionDataSource.fetchShelterDetailResponse(shelter_id)
        }
    )
        fun getAllShelterdetailViewAllLiveData(shelter_id: String) = resultLiveData(
            networkCall = {
                remoteAdoptionDataSource.fetchShelterDetailViewAllResponse(shelter_id)
            }
    )

}
package com.aks.doggydoo.adoptdogdetails.datasource.repo

import com.aks.doggydoo.adoption.datasource.repo.RemoteAdoptionDataSource
import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdoptionDogDetailRepo @Inject
constructor(
    private var remoteAdoptionDogDataSource: RemoteAdoptionDogDataSource
) {
    fun getAdoptionDogDetailLiveData(
        adoptId: String,
        userid : String
    ) = resultLiveData(
        networkCall = { remoteAdoptionDogDataSource.fetchAdoptionDogDetailResponse(adoptId,userid) }
    )

    fun getSendAdoptionLiveData(
        userId: String,
        receiveId: String,
        petId: String
    ) = resultLiveData(
        networkCall = {
            remoteAdoptionDogDataSource.fetchSendAdoptionDogResponse(
                userId,
                receiveId,
                petId
            )
        }
    )

}
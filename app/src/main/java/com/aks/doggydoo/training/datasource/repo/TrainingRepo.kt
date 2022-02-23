package com.aks.doggydoo.training.datasource.repo

import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepo @Inject constructor(
    private var remoteTrainingDataSource: RemoteTrainingDataSource
) {
    fun trainingListLiveData() = resultLiveData(
        networkCall = { remoteTrainingDataSource.fetchTrainingListResponse() }
    )

    fun trainingDetailLiveData(id: String) = resultLiveData(
        networkCall = { remoteTrainingDataSource.fetchTrainingDetailResponse(id) }
    )

    fun submitTrainingVideoLiveData(user_id: String, title: String, url: String) = resultLiveData(
        networkCall = { remoteTrainingDataSource.submitTrainingVideoResponse(user_id, title, url) }
    )
}
package com.aks.doggydoo.training.datasource.repo

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteTrainingDataSource  @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler(){
    /**
     * fetch Training list Response from network
     * */
    suspend fun fetchTrainingListResponse(
    ) =
        getResult {
            apiFactory.createService(TrainingApiService::class.java)
                .getTrainingList(
                )
        }
  /**
     * fetch Training Detail Response from network
     * */
    suspend fun fetchTrainingDetailResponse(
      id:String
    ) =
        getResult {
            apiFactory.createService(TrainingApiService::class.java)
                .getDetail(
                    id
                )
        }

    suspend fun submitTrainingVideoResponse(
        user_id:String, title:String, url:String
    ) =
        getResult {
            apiFactory.createService(TrainingApiService::class.java)
                .uploadTrainingVideo(
                    user_id, title,url
                )
        }

}
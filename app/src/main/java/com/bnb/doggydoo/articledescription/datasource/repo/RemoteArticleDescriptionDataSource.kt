package com.bnb.doggydoo.articledescription.datasource.repo

import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteArticleDescriptionDataSource  @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler()  {
    /**
     * fetch comment Response from network
     * */
    suspend fun fetchCommentResponse(
        newsfeed_id: String
    ) =
        getResult {
            apiFactory.createService(ArticleDescriptionApiService::class.java)
                .getComment(
                    newsfeed_id
                )
        }

    suspend fun fetchPostCommentResponse(newsFeedId: String, comment: String, userId: String)  =
        getResult {
            apiFactory.createService(ArticleDescriptionApiService::class.java)
                .postComment(
                    newsFeedId,comment,userId
                )
        }

  suspend  fun fetchCommentCountResponse(newsFeedId: String) =
      getResult {
          apiFactory.createService(ArticleDescriptionApiService::class.java)
              .getCommentCount(
                  newsFeedId
              )
      }
}
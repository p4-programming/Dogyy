package com.aks.doggydoo.articledescription.datasource.repo

import com.aks.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDescriptionRepo @Inject
constructor(
    private var remoteArticleDescriptionDataSource: RemoteArticleDescriptionDataSource
) {
    fun getCommentLiveData(
        newfeedId: String
    ) = resultLiveData(
        networkCall = { remoteArticleDescriptionDataSource.fetchCommentResponse(newfeedId) }
    )

    fun postComment(newsFeedId: String, comment: String, userId: String) = resultLiveData(
        networkCall = {
            remoteArticleDescriptionDataSource.fetchPostCommentResponse(
                newsFeedId,
                comment,userId
            )
        }
    )

    fun getCommentCountLiveData(newsFeedId: String)= resultLiveData ( networkCall = {
        remoteArticleDescriptionDataSource.fetchCommentCountResponse(
            newsFeedId
        )
    })

}
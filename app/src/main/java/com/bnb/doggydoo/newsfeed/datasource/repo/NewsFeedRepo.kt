package com.bnb.doggydoo.newsfeed.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsFeedRepo @Inject
constructor(
    private var remoteNewsFeedDataSource: RemoteNewsFeedDataSource
) {
    fun getNewsFeedByIdLiveData(
        searchBy: String, userid: String,filter: String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.fetchNewsFeedByIdResponse(searchBy, userid, filter) }
    )

    fun getAllNewsFeedLiveData(
        searchBy: String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.fetchAllNewsFeedResponse(searchBy) }
    )

    fun uploadNewFeedLiveData(
        user_id: String,
        caption: String,
        article: String,
        fileType: String,
        photo: MultipartBody.Part,
        news_type: String
    ) = resultLiveData(
        networkCall = {
            remoteNewsFeedDataSource.fetchUploadNewsFeedResponse(
                user_id,
                caption,
                article,
                fileType,
                photo,
                news_type
            )
        }
    )

    fun blockUserLiveData(
        user_id: String, block_id: String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.blockUserResponseResponse(user_id,block_id) }
    )

    fun commentUserLiveData(
        newsfeed_id: String, comment: String,user_id:String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.commentUserResponseResponse(newsfeed_id,comment,user_id) }
    )

    fun likeUserLiveData(
        newsfeed_id: String, like: String,user_id:String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.likeUserResponseResponse(newsfeed_id,like,user_id) }
    )

    fun userCommentListLiveData(
        newsfeed_id: String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.userCommentListResponseResponse(newsfeed_id) }
    )

    fun deletePostLiveData(
        user_id: String,
        newsfeed_id: String
    ) = resultLiveData(
        networkCall = { remoteNewsFeedDataSource.deletePostResponse(user_id,newsfeed_id) }
    )
}
package com.aks.doggydoo.newsfeed.datasource.repo

import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteNewsFeedDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    /**
     * fetch AllNewsFeed Response from network
     * 1=upload, 2=Image, 3= Article
     * */
    suspend fun fetchAllNewsFeedResponse(
        searchBy: String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .getAllNewsFeedList(
                    searchBy
                )
        }

    /**
     * fetch UploadData Response from network
     * */
    suspend fun fetchUploadNewsFeedResponse(
        user_id: String,
        caption: String,
        article: String,
        fileType: String,
        photo: MultipartBody.Part,
        news_type:String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .uploadNewsFeed(
                    MultipartFile.createPartFromString(user_id),
                    MultipartFile.createPartFromString(caption),
                    MultipartFile.createPartFromString(article),
                    MultipartFile.createPartFromString(fileType),
                    photo,
                    MultipartFile.createPartFromString(news_type)
                )
        }

    /**
     * fetch NewsFeedById Response from network
     * 1=upload, 2=Image, 3= Article
     * */
    suspend fun fetchNewsFeedByIdResponse(
        searchBy: String, userid: String, filter:String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .getNewsFeedById(
                    searchBy, userid, filter
                )
        }


    suspend fun blockUserResponseResponse(
        user_id: String, block_id: String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .blockUser(
                    user_id, block_id
                )
        }


    suspend fun commentUserResponseResponse(
        newsfeed_id: String, comment: String,user_id:String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .commentPost(
                    newsfeed_id,comment,user_id
                )
        }


    suspend fun likeUserResponseResponse(
        newsfeed_id: String, like: String,user_id:String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .likePost(
                    newsfeed_id,like,user_id
                )
        }

    suspend fun userCommentListResponseResponse(
        newsfeed_id: String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .getCommentListPost(
                    newsfeed_id
                )
        }

    suspend fun deletePostResponse(
        user_id:String,newsfeed_id: String
    ) =
        getResult {
            apiFactory.createService(NewsFeedApiService::class.java)
                .deletePost(
                    user_id,newsfeed_id
                )
        }
}
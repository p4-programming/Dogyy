package com.bnb.doggydoo.newsfeed.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.newsfeed.datasource.repo.NewsFeedRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(private var newsFeedRepo: NewsFeedRepo) :
    ViewModel() {
    fun getByNewsFeedId(
        searchBy: String, userid: String, filter:String
    ) =
        newsFeedRepo.getNewsFeedByIdLiveData(searchBy, userid, filter)

    fun getAllNewsFeed(
        searchBy: String
    ) =
        newsFeedRepo.getAllNewsFeedLiveData(searchBy)

    fun uploadFeed(
        user_id: String,
        caption: String,
        article: String,
        fileType: String,
        photo: MultipartBody.Part,
        news_type: String
    ) =
        newsFeedRepo.uploadNewFeedLiveData(user_id, caption, article, fileType, photo, news_type)


    fun blockNewsFeedUser(
        user_id: String, block_id: String
    ) =
        newsFeedRepo.blockUserLiveData(user_id,block_id)

    fun commentNewsFeedPost(
        newsfeed_id: String, comment: String,user_id:String
    ) =
        newsFeedRepo.commentUserLiveData(newsfeed_id,comment,user_id)


    fun likeNewsFeedPost(
        newsfeed_id: String, like: String,user_id:String
    ) =
        newsFeedRepo.likeUserLiveData(newsfeed_id,like,user_id)


    fun newsFeedCommentList(
        newsfeed_id: String
    ) =
        newsFeedRepo.userCommentListLiveData(newsfeed_id)

    fun deleteNewsFeedPost(
        user_id: String,
        newsfeed_id: String
    ) =
        newsFeedRepo.deletePostLiveData(user_id,newsfeed_id)
}
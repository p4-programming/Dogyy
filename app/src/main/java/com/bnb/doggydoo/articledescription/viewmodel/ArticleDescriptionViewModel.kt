package com.bnb.doggydoo.articledescription.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.articledescription.datasource.repo.ArticleDescriptionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleDescriptionViewModel @Inject constructor(var articleDescriptionRepo: ArticleDescriptionRepo) :
    ViewModel() {
    fun getCommentData(
        newsFeedId: String
    ) =
        articleDescriptionRepo.getCommentLiveData(newsFeedId)

    fun postComment(newsFeedId: String, comment: String, userId: String) =
        articleDescriptionRepo.postComment(newsFeedId,comment,userId)
fun getCommentCount(newsFeedId: String)= articleDescriptionRepo.getCommentCountLiveData(newsFeedId)
}
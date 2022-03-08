package com.aks.doggydoo.article.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.article.datasource.repo.ArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(var articleRepo: ArticleRepo) :
    ViewModel() {
    fun getInsertedArticleData(
        user_id: String,
        photo: MultipartBody.Part,
        caption: String,
        article: String,
        news_type: String
    ) =
        articleRepo.getInsertedArticleLiveData(user_id, photo, caption, article, news_type)

    fun getArticleDataHome(
        user_id: String
    ) =
        articleRepo.getArticleLiveData(user_id)


    fun getAllArticleDataHome(
        user_id: String, type: String
    ) =
        articleRepo.getAllArticleLiveData(user_id, type)

    fun getSingleArticleDataHome(
        type:String,blog_id: String, user_id: String
    ) =
        articleRepo.getSingleArticleLiveData(type,blog_id, user_id)


    fun getSingleArticleLikeDataHome(
        type: String,blog_id: String, user_id: String, like: String
    ) =
        articleRepo.getSingleArticleLikeLiveData(type,blog_id, user_id, like)


    fun getSingleArticleCommentDataHome(
        type: String,blog_id: String, user_id: String, comment: String
    ) =
        articleRepo.getSingleArticleCommentLiveData(type,blog_id, user_id, comment)

    fun getCommentDataHome(
        blog_id: String
    ) =
        articleRepo.getCommentLiveData(blog_id)
}
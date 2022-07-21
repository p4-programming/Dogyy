package com.bnb.doggydoo.article.datasource.repo

import com.bnb.doggydoo.utils.helper.resultLiveData
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepo @Inject
constructor(
    private var remoteArticleDataSource: RemoteArticleDataSource
) {
    fun getInsertedArticleLiveData(
        user_id: String,
        photo: MultipartBody.Part,
        caption: String,
        article: String,
        news_type:String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchInsertedArticleResponse(
                user_id,
                photo,
                caption,
                article,
                news_type
            )
        }
    )


    fun getArticleLiveData(
        user_id: String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchArticleResponse(
                user_id
            )
        }
    )


    fun getAllArticleLiveData(
        user_id: String,
        type:String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchAllArticleResponse(
                user_id, type
            )
        }
    )

    fun getSingleArticleLiveData(
        type:String,blog_id:String,user_id: String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchSingleArticleResponse(
                type,blog_id, user_id
            )
        }
    )


    fun getSingleArticleLikeLiveData(
     type: String,blog_id:String,user_id: String,like:String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchSingleArticleLikeResponse(
                type, blog_id, user_id,like
            )
        }
    )

    fun getSingleArticleCommentLiveData(
        type: String,blog_id:String,user_id: String,comment:String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchSingleArticleCommentResponse(
                type,blog_id, user_id,comment
            )
        }
    )

    fun getCommentLiveData(
        blog_id:String
    ) = resultLiveData(
        networkCall = {
            remoteArticleDataSource.fetchCommentResponse(
                blog_id
            )
        }
    )
}
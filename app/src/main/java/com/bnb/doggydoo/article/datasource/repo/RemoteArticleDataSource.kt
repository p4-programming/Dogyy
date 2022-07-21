package com.bnb.doggydoo.article.datasource.repo

import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import okhttp3.MultipartBody
import javax.inject.Inject

class RemoteArticleDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch insertedArticle Response from network
     * */
    suspend fun fetchInsertedArticleResponse(
        user_id: String,
        photo: MultipartBody.Part,
        caption: String,
        article: String,
        news_type:String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .insertArticle(
                    user_id = MultipartFile.createPartFromString(user_id),
                    photo = photo,
                    caption = MultipartFile.createPartFromString(caption),
                    article = MultipartFile.createPartFromString(article),
                    news_type = MultipartFile.createPartFromString(news_type)
                )
        }


    suspend fun fetchArticleResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getAricleDataHome(
                    user_id
                )
        }

    suspend fun fetchAllArticleResponse(
        user_id: String, type:String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getAricleViewAll(
                    user_id,type
                )
        }


    suspend fun fetchSingleArticleResponse(
        type:String,blog_id:String,user_id: String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getSingleAricleDetail(
                  type,blog_id, user_id
                )
        }


    suspend fun fetchSingleArticleLikeResponse(
       type: String,blog_id:String,user_id: String, like:String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getAricleLikeDetail(
                    type,blog_id, user_id,like
                )
        }


    suspend fun fetchSingleArticleCommentResponse(
        type: String,blog_id:String,user_id: String, comment:String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getAricleCommentDetail(
                    type,blog_id, user_id,comment
                )
        }

    suspend fun fetchCommentResponse(
        blog_id:String
    ) =
        getResult {
            apiFactory.createService(ArticleApiService::class.java)
                .getAricleComment(
                    blog_id
                )
        }
}
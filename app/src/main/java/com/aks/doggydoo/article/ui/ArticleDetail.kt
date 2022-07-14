package com.aks.doggydoo.article.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.article.datasource.model.Articledetail
import com.aks.doggydoo.article.viewmodel.ArticleViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityArticleDetailBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetail : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding
    private lateinit var articleViewModel: ArticleViewModel
    private var articleId: String = ""
    private var type: String=""
    private var articleDetail: String = ""
    private var articleTitle: String = ""
    private var articleImage: String = ""
    private var isLiked: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        callArticleAPI()
    }

    private fun getInit() {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        articleId = intent.getStringExtra("article_id").toString()
        type= intent.getStringExtra("type").toString()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.navIcon.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                articleTitle + "\n" + articleDetail + "\n" + articleImage
            )
            startActivity(Intent.createChooser(intent, "Share with:"))
        }

        binding.llLike.setOnClickListener {
            if (isLiked == "1") {
                callArticleLikeAPI("2")
            } else {
                callArticleLikeAPI("1")
            }

        }

        binding.llComment.setOnClickListener {
            //CommentDialog()
            startActivity(
                Intent(this, ViewBlogCommentActivity::class.java)
                    .putExtra("blogId", articleId)
                    .putExtra("blog_type",type)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }


    override fun onResume() {
        super.onResume()
        callArticleAPI()
    }

    private fun callArticleAPI() {
        println("data is>>" + articleId + MyApp.getSharedPref().userId)
        articleViewModel.getSingleArticleDataHome(type,articleId, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        renderDate(it.data.singleblogDataList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun renderDate(data: List<Articledetail>) {
        if (data.isNotEmpty()) {
            articleDetail = data[0].article
            articleTitle = data[0].caption
            isLiked = data[0].like
            if (isLiked == "1") {
                binding.likeHeart.setColorFilter(ContextCompat.getColor(this, R.color.red))
            } else {
                binding.likeHeart.setColorFilter(ContextCompat.getColor(this, R.color.black))
            }

            if (data[0].file.isEmpty()) {
                binding.ivArticle.visibility = View.GONE
            } else {
                articleImage = ApiConstant.BLOG_IMAGE_BASE_URL + data[0].file
                binding.ivArticle.visibility = View.VISIBLE
                binding.ivArticle.loadImageFromString(
                    this,
                    ApiConstant.BLOG_IMAGE_BASE_URL + data[0].file
                )
            }
            binding.tvTitle.text = data[0].caption
            binding.tvDescription.text = data[0].article
            binding.tvLikeCount.text = data[0].countlike
            binding.tvCommentCount.text = data[0].commentcount
            //binding.tvUserName.text = data[0].username + "'" + " Article"
        } else {
            Toast.makeText(this, "No data found!!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun callArticleLikeAPI(isLike: String) {
        articleViewModel.getSingleArticleLikeDataHome(type,articleId, MyApp.getSharedPref().userId, isLike
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        it.data.responseMessage.snack(Color.RED, binding.parent)
                        callArticleAPI()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

}
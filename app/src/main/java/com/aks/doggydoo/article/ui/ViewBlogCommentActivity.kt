package com.aks.doggydoo.article.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.article.adapter.AllCommentAdapter
import com.aks.doggydoo.article.viewmodel.ArticleViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityViewBlogCommentBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewBlogCommentActivity : AppCompatActivity() {
    private var _binding: ActivityViewBlogCommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel: ArticleViewModel
    private var articleId:String =""
    private lateinit var adapter: AllCommentAdapter
    private var blogType: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewBlogCommentBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(binding.root)
        getInit()
        callCommentAPI()

    }

    private fun getInit() {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        binding.backButton.setOnClickListener {
            finish()
        }

        articleId = intent.getStringExtra("blogId").toString()
        blogType = intent.getStringExtra("blog_type").toString()
        adapter = AllCommentAdapter(this@ViewBlogCommentActivity)
        binding.rvComment.adapter = adapter

        binding.ivSend.setOnClickListener {
            if (binding.etComment.text.trim().isEmpty()) {
                Toast.makeText(this, "Please mention your comment.", Toast.LENGTH_SHORT).show()
            } else {
                commentPost(binding.etComment.text.toString())
            }
        }
    }

    private fun commentPost(comment: String) {
        articleViewModel.getSingleArticleCommentDataHome(blogType, articleId, MyApp.getSharedPref().userId,comment)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        "Commented".snack(Color.RED, binding.parent)
                        binding.etComment.setText("")

                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        callCommentAPI()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun callCommentAPI() {
        articleViewModel.getCommentDataHome(articleId)
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
                        adapter.submitList(it.data.blogcommentList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}
package com.bnb.doggydoo.newsfeed.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityArticleDetailsBinding
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private var viewType: String = ""
    private var url: String = ""
    private var caption: String = ""
    private var desc: String = ""
    private var likeCount: Int = 0
    private var commentCount: Int = 0
    private var newsFeedId: String = ""
    private var isLiked: String = ""
    private lateinit var newsFeedViewModel: NewsFeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityArticleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)

        binding.backButton.setOnClickListener {
            finish()
        }

        if (intent != null)
            viewType = intent.getStringExtra("type").toString()
            url = intent.getStringExtra("url").toString()
            caption = intent.getStringExtra("caption").toString()
            desc = intent.getStringExtra("description").toString()
            likeCount = intent.getStringExtra("likeCount")!!.toInt()
            commentCount = intent.getStringExtra("commentCount")!!.toInt()
            newsFeedId = intent.getStringExtra("newsfeedId").toString()
            isLiked = intent.getStringExtra("isLiked").toString()

            binding.tvlike1.text = likeCount.toString()
            binding.tvComment1.text = commentCount.toString()
            binding.tvLike.text= isLiked
            binding.tvComment.text= commentCount.toString()

        // Toast.makeText(this, isLiked, Toast.LENGTH_SHORT).show()
        if (isLiked == "1") {
            binding.ivDoLike1.setColorFilter(ContextCompat.getColor(this, R.color.red))
        } else {
            binding.ivDoLike1.setColorFilter(ContextCompat.getColor(this, R.color.black))

        }

        binding.rlDoLike1.setOnClickListener {
            if (isLiked == "1") {
                likePost(newsFeedId, "2")
            } else {
                likePost(newsFeedId, "1")
            }
        }

        if (viewType == "image") {
            binding.vvArticle.hide()
            binding.ivImage.show()
            binding.llArticle.hide()

            binding.ivImage.loadImageFromString(
                this, url
            )

        } else if (viewType == "video") {
            binding.vvArticle.show()
            binding.ivImage.hide()
            binding.llArticle.hide()

            binding.vvArticle.showControls()
            binding.vvArticle.setSource(Uri.parse(url))
            binding.vvArticle.start()
            binding.vvArticle.setAutoPlay(true)

        } else {
            binding.llArticle.show()
            binding.vvArticle.hide()
            binding.ivImage.hide()
            binding.ArticleImage.loadImageFromString(
                this, url
            )
            binding.tvCaption.text = caption
            binding.tvDescription.text = desc

        }



        binding.rlDoComment1.setOnClickListener {

            startActivity(

                Intent(this, CommentActivity::class.java)
                    .putExtra("newsFeedId", newsFeedId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )

        }

    }

    override fun onPause() {
        super.onPause()
        binding.vvArticle.pause()
    }

    private fun likePost(newsfeedId: String, type: String) {
        newsFeedViewModel.likeNewsFeedPost(newsfeedId, type, MyApp.getSharedPref().userId).observe(
            this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == ("0")) {
                            return@Observer
                        }
                        it.data.responseMessage.snack(Color.BLACK, binding.parent)
                        if (type == "1"){
                            likeCount = likeCount + 1
                            binding.ivDoLike1.setColorFilter(ContextCompat.getColor(this, R.color.red))
                            isLiked = "1"
                        }else{
                            likeCount = likeCount - 1
                            binding.ivDoLike1.setColorFilter(ContextCompat.getColor(this, R.color.black))
                            isLiked = "2"
                        }

                        binding.tvlike1.text = likeCount.toString()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }
}
package com.aks.doggydoo.articledescription.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.articledescription.viewmodel.ArticleDescriptionViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityArticleDesriptionBinding
import com.aks.doggydoo.newsfeed.adapter.UserCommentAdapter
import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedDetail
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDescription : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDesriptionBinding
    private var newsFeedDetail: NewsFeedDetail?=null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var adapter: UserCommentAdapter
    private lateinit var articleDescriptionViewModel: ArticleDescriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDesriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        articleDescriptionViewModel = ViewModelProvider(this).get(ArticleDescriptionViewModel::class.java)

        newsFeedDetail = intent.getParcelableExtra("newsFeedDetail")!!
        binding.content.text = newsFeedDetail?.article
        binding.title.text = newsFeedDetail?.caption
        setBottomSheet()
        binding.comment.setOnClickListener {
            binding.commentBsLayout.bottomSheet.show()
        }
        binding.commentBsLayout.etComment.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                callPostCommentAPI()
            }
            true
        }
        binding.backButton.setOnClickListener { finish() }
    }

    private fun setBottomSheet() {
        //Initialized bottomSheetBehavior here!!
        bottomSheetBehavior =
            BottomSheetBehavior.from<RelativeLayout>(binding.commentBsLayout.bottomSheet)

        initializeBottomSheetAdapters()
        //bottomSheetBehavior listener
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.commentBsLayout.rectangle.hide()
                    binding.commentBsLayout.bottomSheet.show()
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.commentBsLayout.bottomSheet.hide()
                } else {
                    binding.commentBsLayout.rectangle.show()
                }
            }
        })

        binding.commentBsLayout.etComment.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }

    }

    private fun initializeBottomSheetAdapters() {
        //Initialized HomePlayDateAdapter here!!
        adapter = UserCommentAdapter(this)
        binding.commentBsLayout.commentRv.adapter = adapter
        callGetCommentApi()
    }

    private fun callGetCommentApi() {
        articleDescriptionViewModel.getCommentData(newsFeedDetail?.id!!).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.commentBsLayout.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    if (it.data?.responseCode.equals("0")) {
                        it.data?.responseMessage?.snack(
                            Color.RED,
                            binding.commentBsLayout.bottomSheet
                        )
                        return@Observer
                    }
                    binding.commentBsLayout.progressBar.hide()
                    adapter.submitList(it.data?.newsfeedCommentdetail)
                }
                Result.Status.ERROR -> {
                    binding.commentBsLayout.progressBar.hide()
                    it.message?.snack(Color.RED, binding.commentBsLayout.bottomSheet)
                }
            }
        })
    }

    private fun callPostCommentAPI() {
        articleDescriptionViewModel.postComment(
            newsFeedDetail?.id!!, binding.commentBsLayout.etComment.text.toString().trim(),
            MyApp.getSharedPref().userId
        ).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.commentBsLayout.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.commentBsLayout.etComment.setText("")
                    if (it.data?.responseCode.equals("0")) {
                        it.data?.responseMessage?.snack(
                            Color.RED,
                            binding.commentBsLayout.bottomSheet
                        )
                        return@Observer
                    }
                    "Comment Posted".snack(R.color.docbuton, binding.commentBsLayout.bottomSheet)
                    callGetCommentApi()
                }
                Result.Status.ERROR -> {
                    binding.commentBsLayout.progressBar.hide()
                    it.message?.snack(Color.RED, binding.commentBsLayout.bottomSheet)
                }
            }
        })
    }

}
package com.aks.doggydoo.newsfeed.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityCommentBinding
import com.aks.doggydoo.newsfeed.adapter.UserAdapter
import com.aks.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentActivity : AppCompatActivity() {
    private var _binding: ActivityCommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsFeedViewModel: NewsFeedViewModel
    private var newsFeedId: String = ""
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getComment()
    }

    private fun getInit() {
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)
        newsFeedId = intent.getStringExtra("newsFeedId").toString()
        adapter = UserAdapter(this@CommentActivity)
        binding.rvComment.adapter = adapter
        //binding.rvComment.scrollToPosition(adapter.itemCount - 1)


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.ivSend.setOnClickListener {
            if (binding.etComment.text.trim().isEmpty()) {
                Toast.makeText(this, "Please mention your comment.", Toast.LENGTH_SHORT).show()
            } else {
                commentPost(newsFeedId)
            }
        }
    }

    private fun commentPost(newsfeedId: String) {
        newsFeedViewModel.commentNewsFeedPost(
            newsfeedId,
            binding.etComment.text.toString().trim(),
            MyApp.getSharedPref().userId
        ).observe(
            this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.etComment.setText("")

                        hideKeyboard()

                        binding.rvComment.postDelayed({
                            binding.rvComment.scrollToPosition(adapter.itemCount - 1)
                        }, 1000)

                        /*binding.progressBar.hide()
                        "Commented".snack(Color.RED, binding.parent)
                        binding.etComment.setText("")*/

                        if (it.data!!.responseCode == ("0")) {
                            return@Observer
                        }
                        getComment()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getComment() {
        newsFeedViewModel.newsFeedCommentList(newsFeedId).observe(
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
                        adapter.submitList(it.data.newsfeedcommentList)
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
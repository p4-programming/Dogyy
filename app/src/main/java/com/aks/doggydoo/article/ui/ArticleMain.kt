package com.aks.doggydoo.article.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.article.adapter.FriendsBlogAdapter
import com.aks.doggydoo.article.adapter.MyArticleAdapter
import com.aks.doggydoo.article.adapter.TrendingAdapter
import com.aks.doggydoo.article.datasource.model.ArticleHomeResponse
import com.aks.doggydoo.article.viewmodel.ArticleViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityArticleBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleMain : Fragment() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var friendblogAdapter: FriendsBlogAdapter
    private lateinit var myblogAdapter: MyArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callInsertArticleAPI()
    }

    override fun onResume() {
        super.onResume()
        callInsertArticleAPI()
    }

    private fun getInit() {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        trendingAdapter = TrendingAdapter(requireContext())
        binding.trendRv.adapter = trendingAdapter
        friendblogAdapter = FriendsBlogAdapter(requireContext())
        binding.friendsRaticleRv.adapter = friendblogAdapter
        myblogAdapter = MyArticleAdapter(requireContext())
        binding.yourArticleRv.adapter = myblogAdapter


        binding.add.setOnClickListener {
            startActivity(
                Intent(requireContext(), ArticleUpload::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.backButton.setOnClickListener { requireActivity().finish() }

        binding.viewAllTreding.setOnClickListener {
            navigateTo("trending")
        }
        binding.viewAllFriendBlog.setOnClickListener {
            navigateTo("friends")
        }
        binding.viewAllYourArticle.setOnClickListener {
            navigateTo("userblog")
        }
    }

    private fun navigateTo(type: String) {
        startActivity(
            Intent(context, ViewAllArticleActivity::class.java).putExtra("viewType", type)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun callInsertArticleAPI() {
        articleViewModel.getArticleDataHome(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner, Observer {
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
                        renderDate(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun renderDate(data: ArticleHomeResponse) {
        if (data.trendingblogList.size > 0) {
            binding.tvTrend.visibility = View.GONE
            trendingAdapter.submitList(data.trendingblogList)
        } else {
            binding.tvTrend.visibility = View.VISIBLE
        }

        if (data.friendsblogList.size > 0) {
            binding.tvFriend.visibility = View.GONE
            friendblogAdapter.submitList(data.friendsblogList)
        } else {
            binding.tvFriend.visibility = View.VISIBLE
        }

        if (data.userblogList.size > 0) {
            binding.tvArticles.visibility = View.GONE
            myblogAdapter.submitList(data.userblogList)
        } else {
            binding.tvArticles.visibility = View.VISIBLE
        }
    }

}
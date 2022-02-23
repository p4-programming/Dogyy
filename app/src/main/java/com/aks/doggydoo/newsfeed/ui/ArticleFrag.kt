package com.aks.doggydoo.newsfeed.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentArticleBinding
import com.aks.doggydoo.newsfeed.adapter.NewsFeedDataAdapter
import com.aks.doggydoo.newsfeed.datasource.model.NewsFeedDetail
import com.aks.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFrag : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsFeedViewModel: NewsFeedViewModel
    private lateinit var adapter: NewsFeedDataAdapter
    private var filterType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        getNewsFeedData()
    }

    private fun getInit() {
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)

        adapter = NewsFeedDataAdapter(requireContext(), MyApp.getSharedPref().newsTypeFilter) { blockUserId, type ->
            when (type) {
                "block" -> {
                    blockUser(blockUserId)
                }
                "delete" -> {
                    deletePost(blockUserId, "1")
                }
                "like" -> {
                    likePost(blockUserId, "1")
                }
                else -> {
                    likePost(blockUserId, "2")
                }
            }


        }
        binding.rvArticleItem.adapter = adapter

        //** Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.rvArticleItem.clearAnimation()
            getNewsFeedData()
            adapter = NewsFeedDataAdapter(requireContext(), MyApp.getSharedPref().newsTypeFilter) { blockUserId, type ->
                when (type) {
                    "block" -> {
                        blockUser(blockUserId)
                    }
                    "delete" -> {
                        deletePost(blockUserId, "1")
                    }
                    "like" -> {
                        likePost(blockUserId, "1")
                    }
                    else -> {
                        likePost(blockUserId, "2")
                    }
                }

            }

            binding.rvArticleItem.adapter = adapter
            binding.itemsswipetorefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        getNewsFeedData()
    }

    private fun getNewsFeedData() {
        filterType = when (MyApp.getSharedPref().newsTypeFilter) {
            "All" -> {
                "1"
            }
            "My Post" -> {
                "3"
            }
            else -> {
                "2"
            }
        }
        newsFeedViewModel.getByNewsFeedId("3", MyApp.getSharedPref().userId, filterType).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                            binding.noData.show()
                            return@Observer
                        }
                        renderList(it.data.newsfeeddetail)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )

    }

    private fun renderList(newsfeeddetail: List<NewsFeedDetail>) {
        adapter.addData(newsfeeddetail)

    }

    private fun blockUser(blockUserId: String) {
        newsFeedViewModel.blockNewsFeedUser(MyApp.getSharedPref().userId, blockUserId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == ("0")) {
                            binding.noData.show()
                            return@Observer
                        }
                        getNewsFeedData()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }

    private fun likePost(newsfeedId: String, type: String) {
        newsFeedViewModel.likeNewsFeedPost(newsfeedId, type, MyApp.getSharedPref().userId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                            it.message?.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        getNewsFeedData()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }

    private fun deletePost(newsfeedId: String, type: String) {
        newsFeedViewModel.deleteNewsFeedPost(MyApp.getSharedPref().userId, newsfeedId).observe(
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        it.data.responseMessage.snack(Color.BLACK, binding.parent)
                        getNewsFeedData()
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
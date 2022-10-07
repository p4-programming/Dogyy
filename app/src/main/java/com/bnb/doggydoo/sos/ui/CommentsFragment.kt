package com.bnb.doggydoo.sos.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentCommentsBinding
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.myprofile.datasource.model.profile.NewUploadData
import com.bnb.doggydoo.myprofile.datasource.model.profile.UserDetail
import com.bnb.doggydoo.newsfeed.adapter.UserAdapter
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedCommentDetail
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.sos.ui.adapter.CommentAdapter
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsFeedViewModel: NewsFeedViewModel
    private var newsFeedId: String = "null"
    private var name: String = ""
    private var mobile: String = ""
    private var pic: String = ""
    private var cdate: String = ""
    private lateinit var recyclerAdapter: CommentAdapter
    private lateinit var DataList: ArrayList<NewsFeedCommentDetail>
    private lateinit var id: NewUploadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        getInit()
        getComment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvComment.setHasFixedSize(true)
        binding.rvComment.layoutManager = LinearLayoutManager(context)
        DataList = ArrayList()
    }

    private fun getInit() {
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)
        newsFeedId = id.id
        Log.d("Deepak","NewsFeedId : $newsFeedId")

    }

    private fun getComment() {
        newsFeedViewModel.newsFeedCommentList(newsFeedId).observe (
            viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == ("0")) {
                            return@Observer
                        }
//                        adapter.submitList(it.data.newsfeedcommentList)
                        Dataset(it.data.newsfeedcommentList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            }
        )
    }


    private fun Dataset(sdata: List<NewsFeedCommentDetail>){
        DataList.clear()
        name = sdata[0].username
        pic = sdata[0].userphoto
        cdate = sdata[0].createon
        mobile = sdata[0].comment

        DataList.addAll(sdata)
        recyclerAdapter = CommentAdapter(context!!,DataList)
        binding.rvComment.adapter = recyclerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
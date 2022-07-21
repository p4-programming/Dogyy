package com.bnb.doggydoo.homemodule.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentMyFriendsBinding
import com.bnb.doggydoo.homemodule.adapter.MyFriendAdapter
import com.bnb.doggydoo.homemodule.datasource.model.home.FriendReqList
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class
MyFriendsFrag : Fragment(R.layout.fragment_my_friends) {
    private lateinit var binding: FragmentMyFriendsBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var myFriendAdapter: MyFriendAdapter

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFriendsBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        getAllFriendApi()
    }

    override fun onResume() {
        super.onResume()
        getAllFriendApi()
    }

    private fun getInit() {
        myFriendAdapter = MyFriendAdapter(requireContext()) { requestId, reqStatus ->
            logoutDialog(requestId, reqStatus)
        }
        binding.friendsRv.adapter = myFriendAdapter


        binding.ivAllFriend.setOnClickListener {
            startActivity(Intent(requireContext(), AllUserListActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }


        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                myFriendAdapter.filter.filter(s)
                myFriendAdapter.notifyDataSetChanged()
            }
        })


        //** Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.friendsRv.clearAnimation()
            getAllFriendApi()
            myFriendAdapter = MyFriendAdapter(requireContext()) { requestId, reqStatus ->
                logoutDialog(requestId, reqStatus)
            }
            binding.friendsRv.adapter = myFriendAdapter
            binding.itemsswipetorefresh.isRefreshing = false
        }
    }


    private fun getAllFriendApi() {
        homeViewModel.getAllFriendResponse(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            binding.noData.hide()

                            if (it.data!!.responseCode == "0") {
                                binding.noData.show()
                                return@Observer
                            }

                            renderList(it.data.allReqList)
                            binding.tvAllFriendCount.text = "All Friends("+it.data.total_accepte_request+")"
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            binding.noData.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    private fun renderList(allReqList: List<FriendReqList>) {
            myFriendAdapter.addData(allReqList)
    }


    @SuppressLint("SetTextI18n")
    private fun logoutDialog(requestId: String, reqStatus: String) {
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm)

        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val yesButton = dialog.findViewById<View>(R.id.btYes) as Button
        val noButton = dialog.findViewById<View>(R.id.btNo) as Button
        val cancel = dialog.findViewById<View>(R.id.ivCancel) as ImageView
        when (reqStatus) {
            "1" -> {
                text.text = "Are you sure want to Accept?"
            }
            "2" -> {
                text.text = "Are you sure want to Reject?"
            }
            else -> {
                text.text = "Are you sure want to Unfriend?"
            }
        }


        yesButton.setOnClickListener {
            callAcceptOrRejectAPI(requestId, reqStatus)
            dialog.dismiss()
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun callAcceptOrRejectAPI(requestId: String, reqStatus: String) {
        homeViewModel.AcceptRejectFriend(requestId, reqStatus)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            if (it.data!!.responseCode == "0") {
                                return@Observer
                            }
                            getAllFriendApi()
                            if (reqStatus == "1")
                                CommonMethod.showSnack(binding.root, "Request accepted.")
                            else
                                CommonMethod.showSnack(binding.root, "Request rejected.")
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }
}
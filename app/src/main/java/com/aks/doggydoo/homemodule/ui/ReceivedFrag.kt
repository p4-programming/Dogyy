package com.aks.doggydoo.homemodule.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.chatMessage.viewmodel.RequestViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ReceivedFragBinding
import com.aks.doggydoo.firebaseChat.ChatActivity
import com.aks.doggydoo.homemodule.adapter.RequestAdapter
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceivedFrag : Fragment(R.layout.received_frag) {
    private var _binding: ReceivedFragBinding? = null
    private lateinit var adapter: RequestAdapter

    private val requestViewModel: RequestViewModel by viewModels()

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReceivedFragBinding.inflate(layoutInflater)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callReceiveRequestApi()
    }

    private fun getInit() {
        adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus, petId , userId,
                                                     userName, userUID,userImage->
            callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
        }

        binding.receivedRv.adapter = adapter

        //** Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.receivedRv.clearAnimation()
            callReceiveRequestApi()
            adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus,petId, userId,
                                                         userName, userUID,userImage ->
                callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
            }

            binding.receivedRv.adapter = adapter
            binding.itemsswipetorefresh.isRefreshing = false
        }
    }

    private fun callAcceptOrRejectAPI(
        requestId: String,
        reqType: String,
        reqStatus: String,
        petId: String,
        userId: String,
        userName: String,
        userUID: String,
        userImage: String,
    ) {
        requestViewModel.acceptOrReject(requestId, reqType, reqStatus,userId,petId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            if (it.data!!.responseCode == "0") {
                                //binding.tvNoData.text = it.data.responseMessage
                                it.data.responseMessage.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus, petId , userId,
                                                                         userName, userUID,userImage->
                                callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
                            }

                            binding.receivedRv.adapter = adapter
                            adapter.notifyDataSetChanged()
                            callReceiveRequestApi()
                            if (reqStatus == "1"){
                                CommonMethod.showSnack(binding.root, "Successfully accepted.")
                                navigateTo(userUID,userName, userImage,reqType)
                            }else{
                                CommonMethod.showSnack(binding.root, "Successfully rejected.")
                            }

                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    private fun navigateTo(userUID: String, userName: String, userImage: String, reqType: String) {
        if (userUID.isBlank()){
            Toast.makeText(requireContext(), "User is not registered.", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(
                Intent(requireContext(), ChatActivity::class.java)
                    .putExtra("name", userName)
                    .putExtra("uid", userUID)
                    .putExtra("userImage", userImage)
                    .putExtra("from", "1")
                    .putExtra("reqType", reqType)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )

        }
    }

    private fun callReceiveRequestApi() {
        requestViewModel.receiveRequestData(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            if (it.data!!.responseCode == "0") {
                                binding.tvNoData1.show()
                                binding.tvNoData.text = it.data.responseMessage
                               // it.data.responseMessage.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            binding.tvNoData1.hide()
                            adapter.submitList(it.data.receiveRequest)
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            binding.tvNoData1.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.bnb.doggydoo.chatMessage

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bnb.doggydoo.R
import com.bnb.doggydoo.chatMessage.viewmodel.RequestViewModel
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ReceivedFragBinding
import com.bnb.doggydoo.homemodule.adapter.RequestAdapter
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SentFrag : Fragment(R.layout.received_frag) {
    private var _binding: ReceivedFragBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RequestAdapter
    private val requestViewModel: RequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReceivedFragBinding.inflate(layoutInflater)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callSentRequestApi()


    }

    private fun getInit() {
        adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus,petId, userId,
                                                     userName, userUID,userImage ->
            callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
        }
        binding.receivedRv.adapter = adapter
        adapter.from("sentRequest")

        //** Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.receivedRv.clearAnimation()
            callSentRequestApi()
            adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus,petId, userId,
                                                         userName, userUID,userImage ->
                callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
            }

            binding.receivedRv.adapter = adapter
            adapter.from("sentRequest")
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
        userImage: String
    ) {

        System.out.println("data is>>" + requestId)
        System.out.println("data is>>" + reqType)
        System.out.println("data is>>" + reqStatus)
        System.out.println("data is>>" + petId)

        requestViewModel.acceptOrReject(requestId, reqType, reqStatus,userId, petId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            if (it.data!!.responseCode == "0") {
                                it.data.responseMessage.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            adapter = RequestAdapter(requireContext()) { requestId, reqType, reqStatus,petId, userId,
                                                                         userName, userUID,userImage ->
                                callAcceptOrRejectAPI(requestId, reqType, reqStatus, petId, userId,userName,userUID,userImage)
                            }

                            binding.receivedRv.adapter = adapter
                            adapter.from("sentRequest")
                            adapter.notifyDataSetChanged()
                            callSentRequestApi()
                            if (reqStatus == "1")
                                CommonMethod.showSnack(binding.root, "Successfully accepted.")
                            else
                                CommonMethod.showSnack(binding.root, "Successfully rejected.")
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    private fun callSentRequestApi() {
        requestViewModel.sentRequestData(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            if (it.data!!.responseCode == "0") {
                                binding.tvNoData1.show()
                                binding.tvNoData.text = it.data.responseMessage
                               // it.data.responseMessage.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            binding.tvNoData1.hide()
                            adapter.submitList(it.data.sentRequest)
                        }
                        Result.Status.ERROR -> {
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

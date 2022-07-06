package com.aks.doggydoo.chatMessage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.chatMessage.viewmodel.CallListViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentCallsBinding
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallsFragment : Fragment(R.layout.fragment_calls) {


    private var _binding: FragmentCallsBinding? = null
    private val binding get() = _binding!!
    private val requestViewModel: CallListViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCallsBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCalls.adapter = CallsAdapter(requireContext())
        callsListRequestApi()
    }

    private fun callsListRequestApi() {
        requestViewModel.callListData(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                        }
                        Result.Status.SUCCESS -> {
                            Toast.makeText(requireActivity(), "it.data!!.responseCode", Toast.LENGTH_SHORT).show()
                            if (it.data!!.responseCode == "0") {
                                //  binding.tvNoData1.show()
                                // binding.tvNoData.text = it.data.responseMessage
                                // it.data.responseMessage.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            //binding.tvNoData1.hide()
                            //   adapter.submitList(it.data.sentRequest)
                        }
                        Result.Status.ERROR -> {
                            //    binding.tvNoData1.hide()
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
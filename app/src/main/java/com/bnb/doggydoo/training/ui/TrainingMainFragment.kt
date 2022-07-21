package com.bnb.doggydoo.training.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentTrainingMainBinding
import com.bnb.doggydoo.training.adapter.TrainingVideoAdapter
import com.bnb.doggydoo.training.viewmodel.TrainingViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingMainFragment : Fragment() {
    private lateinit var adapter: TrainingVideoAdapter
    private lateinit var binding: FragmentTrainingMainBinding
    private val trainingViewModel: TrainingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainingMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonMethod.makeTransparentStatusBar(activity?.window)
        getInit()
        callTrainingListAPI()
    }

    private fun getInit() {
        binding.backButton.setOnClickListener { requireActivity().finish() }

        adapter = TrainingVideoAdapter(requireActivity())
        binding.rvTrainingVideos.adapter = adapter

       /* binding.ivSubmitVideo.setOnClickListener {
            startActivity(
                Intent(requireContext(), UploadVideoUrlActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }*/
    }

    override fun onResume() {
        super.onResume()
        callTrainingListAPI()
    }

    private fun callTrainingListAPI() {
        trainingViewModel.getTrainingData().observe(viewLifecycleOwner, Observer {

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
                    adapter.submitList(it.data.trainingdetail)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message!!.snack(Color.RED, binding.root)
                }
            }
        })
    }
}
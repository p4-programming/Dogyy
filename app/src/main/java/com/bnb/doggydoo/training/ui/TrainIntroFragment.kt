package com.bnb.doggydoo.training.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.FragmentTrainIntroBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bumptech.glide.Glide

class TrainIntroFragment : Fragment() {
    private lateinit var binding: FragmentTrainIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding.apply {
            Glide.with(requireActivity()).asGif().load(R.raw.training).centerCrop().into(
                ivTraining
            )
        }
    }
}
package com.aks.doggydoo.training.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.FragmentTrainIntroBinding
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
        binding.apply {
            Glide.with(requireActivity()).asGif().load(R.raw.training).centerCrop().into(
                ivTraining
            )
        }
    }
}
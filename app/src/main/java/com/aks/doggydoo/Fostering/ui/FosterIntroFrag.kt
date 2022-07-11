package com.aks.doggydoo.fostering.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.FragmentFosterBinding
import com.bumptech.glide.Glide

class FosterIntroFrag : Fragment() {
    private lateinit var binding: FragmentFosterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFosterBinding.inflate(layoutInflater)
        binding.apply {
//            Glide.with(requireActivity()).asGif().load(R.raw.fostering).centerCrop().into(
//                ivTraining
//            )
        }
        return binding.root
    }
}
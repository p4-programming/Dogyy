package com.bnb.doggydoo.fostering.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.databinding.FragmentFosterBinding
import com.bnb.doggydoo.utils.CommonMethod

class FosterIntroFrag : Fragment() {
    private lateinit var binding: FragmentFosterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding = FragmentFosterBinding.inflate(layoutInflater)
        binding.apply {
//            Glide.with(requireActivity()).asGif().load(R.raw.fostering).centerCrop().into(
//                ivTraining
//            )
        }
        return binding.root
    }
}
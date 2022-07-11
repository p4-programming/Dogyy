package com.aks.doggydoo.dogsitting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.FragmentDogSittingIntroBinding
import com.bumptech.glide.Glide

class DogSittingIntroFrag : Fragment() {
   private lateinit var binding: FragmentDogSittingIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDogSittingIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
//            Glide.with(requireActivity()).asGif().load(R.raw.dog_sitting).centerCrop().into(
//                ivTraining
//            )
        }
    }
}
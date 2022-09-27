package com.bnb.doggydoo.homemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.FragmentFaqsBinding

class Faqs :Fragment(R.layout.fragment_faqs){
    private lateinit var binding: FragmentFaqsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeActivity.menuIcon.visibility = View.GONE
        binding.backButton.setOnClickListener {
            HomeActivity.menuIcon.visibility = View.VISIBLE
            findNavController().popBackStack()
        }
    }
}
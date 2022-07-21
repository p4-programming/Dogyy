package com.bnb.doggydoo.homemodule.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.FragmentPagerClass
import com.bnb.doggydoo.databinding.FragmentRequestBinding

class RequestFrag : Fragment(R.layout.fragment_request) {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
        val pager = FragmentPagerClass(childFragmentManager)
        pager.addFragment(ReceivedFrag(), "Received")
        pager.addFragment(ReceivedFrag(), "Sent")
        viewPager.adapter = pager
        binding.tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
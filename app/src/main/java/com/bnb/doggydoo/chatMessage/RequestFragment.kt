package com.bnb.doggydoo.chatMessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.FragmentPagerClass
import com.bnb.doggydoo.databinding.FragmentChatRequestBinding
import com.bnb.doggydoo.homemodule.ui.ReceivedFrag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFragment: Fragment(R.layout.fragment_chat_request) {

    private var _binding: FragmentChatRequestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatRequestBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
        val pager = FragmentPagerClass(childFragmentManager)
        pager.addFragment(ReceivedFrag(), "Received")
        pager.addFragment(SentFrag(), "Sent")
        viewPager.adapter = pager
        binding.tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package com.bnb.doggydoo.homemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.FragmentSettingBinding
import com.bnb.doggydoo.homemodule.adapter.NotificationAdapter

class SettingFrag : Fragment(R.layout.fragment_setting) {
    private lateinit var binding: FragmentSettingBinding
    private val titleList = listOf( "Map Settings", "Edit Profile")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeActivity.menuIcon.visibility = View.GONE
        binding.settingRv.adapter = NotificationAdapter(requireContext(),"Setting", titleList){}
        binding.backButton.setOnClickListener {
            HomeActivity.menuIcon.visibility = View.VISIBLE
            findNavController().popBackStack() }
    }
}
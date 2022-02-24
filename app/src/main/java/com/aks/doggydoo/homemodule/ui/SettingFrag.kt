package com.aks.doggydoo.homemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.FragmentSettingBinding
import com.aks.doggydoo.homemodule.adapter.NotificationAdapter

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
        binding.settingRv.adapter = NotificationAdapter(requireContext(),"Setting", titleList){}
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }
}
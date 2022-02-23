package com.aks.doggydoo.fostering.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityFosteringBinding
import com.aks.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FosteringActivity : AppCompatActivity() {
    private var _binding: ActivityFosteringBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFosteringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(FosterIntroFrag())
        viewPager.addFragment(FosterMainFrag())
        binding.pager.adapter = viewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
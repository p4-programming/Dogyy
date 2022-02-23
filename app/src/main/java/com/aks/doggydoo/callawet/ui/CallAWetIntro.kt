package com.aks.doggydoo.callawet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityCallAWetBinding
import com.aks.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallAWetIntro : AppCompatActivity() {

    private lateinit var binding: ActivityCallAWetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallAWetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(CallAVetIntroFragment())
        viewPager.addFragment(CallAVetMainFragment())
        binding.pager.adapter = viewPager

    }
}
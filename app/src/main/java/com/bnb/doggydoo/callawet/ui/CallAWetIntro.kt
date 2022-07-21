package com.bnb.doggydoo.callawet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityCallAWetBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallAWetIntro : AppCompatActivity() {

    private lateinit var binding: ActivityCallAWetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallAWetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(CallAVetIntroFragment())
        viewPager.addFragment(CallAVetMainFragment())
        binding.pager.adapter = viewPager

    }
}
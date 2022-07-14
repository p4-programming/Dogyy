package com.aks.doggydoo.sos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityAdoptionIntroBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SOSIntro : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionIntroBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(binding.root)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(SOSIntroFragment())
        viewPager.addFragment(SOSMainFragment())
        binding.pager.adapter = viewPager
    }
}
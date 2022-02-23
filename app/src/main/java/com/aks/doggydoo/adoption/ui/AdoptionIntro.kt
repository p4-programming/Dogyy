package com.aks.doggydoo.adoption.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.databinding.ActivityAdoptionIntroBinding
import com.aks.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptionIntro : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(AdoptionIntroFragment())
        viewPager.addFragment(AdoptionMainFragment())
        binding.pager.adapter = viewPager
    }
}
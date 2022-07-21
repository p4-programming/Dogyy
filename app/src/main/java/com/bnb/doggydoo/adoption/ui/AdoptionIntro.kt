package com.bnb.doggydoo.adoption.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityAdoptionIntroBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptionIntro : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(AdoptionIntroFragment())
        viewPager.addFragment(AdoptionMainFragment())
        binding.pager.adapter = viewPager
    }
}
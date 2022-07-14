package com.aks.doggydoo.training.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.adoption.ui.AdoptionIntroFragment
import com.aks.doggydoo.adoption.ui.AdoptionMainFragment
import com.aks.doggydoo.databinding.ActivityTrainingBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.ViewPager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingActivity: AppCompatActivity() {
    private var _binding: ActivityTrainingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(TrainIntroFragment())
        viewPager.addFragment(TrainingMainFragment())
        binding.pager.adapter = viewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
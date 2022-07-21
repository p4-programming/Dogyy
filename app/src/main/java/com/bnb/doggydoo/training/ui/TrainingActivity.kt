package com.bnb.doggydoo.training.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityTrainingBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
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
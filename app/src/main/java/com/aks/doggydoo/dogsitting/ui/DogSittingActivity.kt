package com.aks.doggydoo.dogsitting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.adoption.ui.AdoptionMainFragment
import com.aks.doggydoo.databinding.ActivityDogSittingBinding
import com.aks.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogSittingActivity : AppCompatActivity() {
    private var _binding: ActivityDogSittingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDogSittingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(DogSittingIntroFrag())
        viewPager.addFragment(DogSittingMainFragment())
        binding.pager.adapter = viewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
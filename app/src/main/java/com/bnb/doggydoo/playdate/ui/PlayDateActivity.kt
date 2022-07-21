package com.bnb.doggydoo.playdate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityPlaydateBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayDateActivity : AppCompatActivity() {
    private var _binding: ActivityPlaydateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaydateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(PlayDateIntroFrag())
        viewPager.addFragment(PlayDateMainFrag())
        binding.pager.adapter = viewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
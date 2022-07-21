package com.bnb.doggydoo.newsfeed.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityNewsfeedBinding
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsfeedActivity: AppCompatActivity()  {
    private var _binding: ActivityNewsfeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsfeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(NewsfeedIntroFrag())
        viewPager.addFragment(TempFragment())
        //viewPager.addFragment(NewsFeedDashboardActivity())
        binding.pager.adapter = viewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.bnb.doggydoo.upload.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.ActivityUploadPhotoArticleBinding
import com.bnb.doggydoo.upload.adapter.UploadAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {
    private var _binding: ActivityUploadPhotoArticleBinding? = null
    private val binding get() = _binding!!
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadPhotoArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()
        binding.ivbackButton.setOnClickListener { onBackPressed() }
        askForCameraPermission()
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            1
        )
    }

    private fun setViewPager() {
        tabLayout = findViewById(R.id.tab_tablayout)
        viewPager = findViewById(R.id.tab_viewpager)
        tabLayout.addTab(tabLayout.newTab().setText("Photo/Video"))
        tabLayout.addTab(tabLayout.newTab().setText("Article"))
        val adapter = UploadAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
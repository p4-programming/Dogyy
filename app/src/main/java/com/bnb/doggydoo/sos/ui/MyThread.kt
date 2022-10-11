package com.bnb.doggydoo.sos.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnb.doggydoo.databinding.ActivityMyThreadBinding
import com.bnb.doggydoo.sos.ui.adapter.ThreadSecAdapter
import com.bnb.doggydoo.utils.CommonMethod
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


val titelArray = arrayOf(
    "Threads",
    "Interacted",
)

@AndroidEntryPoint
class MyThread : AppCompatActivity() {
    private var binding:ActivityMyThreadBinding? = null
    private val bind get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyThreadBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(bind.root)

        bind.ivBack.setOnClickListener {
            finish()
        }
        setViewPager()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewPager() {
        val viewPager = bind.tabViewpager1
        val tabLayout = bind.tabTablayout1

        val adapter = ThreadSecAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titelArray[position]
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
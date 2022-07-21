package com.bnb.doggydoo.chatMessage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityMessageRequestCallBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

val titelArray = arrayOf(
    "Message",
    "Request",
    "Calls"
)

@AndroidEntryPoint
class ChatMessageRequestActivity : AppCompatActivity() {
    private var _binding: ActivityMessageRequestCallBinding? = null
    private val binding get() = _binding!!
    private var origin = ""
    private var reqType = ""
    private var currentItemPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMessageRequestCallBinding.inflate(layoutInflater)
        getInit()

        binding.ivback.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
        setViewPager()
    }

    private fun getInit() {
        origin = intent.getStringExtra("from").toString()

        if (origin.equals("message")) {
            currentItemPos = 0
        } else if (origin.equals("request")) {
            currentItemPos = 1
        } else if (origin.equals("call")) {
            currentItemPos = 2
        } else {
            currentItemPos = 0
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewPager() {
        val viewPager = binding.tabViewpager
        val tabLayout = binding.tabTablayout

        val adapter = ChatRequestCallAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titelArray[position]
        }.attach()

        viewPager.currentItem = currentItemPos

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
        _binding = null
    }
}
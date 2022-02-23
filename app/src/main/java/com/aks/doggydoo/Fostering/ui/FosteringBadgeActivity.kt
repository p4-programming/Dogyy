package com.aks.doggydoo.fostering.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityFosteringBadgeBinding

class FosteringBadgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFosteringBadgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFosteringBadgeBinding.inflate(layoutInflater)

        binding.tvGoBack.setOnClickListener {
            startActivity(Intent(this, FosteringActivity::class.java))
        }

        setContentView(binding.root)
    }
}
package com.bnb.doggydoo.fostering.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityFosteringBadgeBinding
import com.bnb.doggydoo.utils.CommonMethod

class FosteringBadgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFosteringBadgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFosteringBadgeBinding.inflate(layoutInflater)

        CommonMethod.makeTransparentStatusBar(window)
        binding.tvGoBack.setOnClickListener {
            startActivity(Intent(this, FosteringActivity::class.java))
        }

        setContentView(binding.root)
    }
}
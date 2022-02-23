package com.aks.doggydoo.badge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityBadgeWonBinding


class BadgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBadgeWonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadgeWonBinding.inflate(layoutInflater)

        binding.tvGoBack.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}
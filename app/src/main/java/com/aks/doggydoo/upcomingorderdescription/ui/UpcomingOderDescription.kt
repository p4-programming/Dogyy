package com.aks.doggydoo.upcomingorderdescription.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.ActivityUpcomingOderDescriptionBinding
import com.aks.doggydoo.tracking.Tracking

private const val TAG = "orderDescTAG"

class UpcomingOderDescription : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingOderDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingOderDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            supportFinishAfterTransition()
        }
        binding.trackOrderButton.setOnClickListener {
            startActivity(Intent(this,Tracking::class.java))
        }
    }
}
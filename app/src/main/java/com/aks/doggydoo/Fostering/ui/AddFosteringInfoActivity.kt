package com.aks.doggydoo.fostering.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityAddfosteringInfoBinding

class AddFosteringInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddfosteringInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddfosteringInfoBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, FosteringActivity::class.java))
        }

        setContentView(binding.root)
    }
}
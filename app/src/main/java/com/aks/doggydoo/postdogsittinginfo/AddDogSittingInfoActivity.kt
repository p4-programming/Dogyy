package com.aks.doggydoo.postdogsittinginfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.ActivityAddDogsittingBinding

class AddDogSittingInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDogsittingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDogsittingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}
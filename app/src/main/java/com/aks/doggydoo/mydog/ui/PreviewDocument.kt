package com.aks.doggydoo.mydog.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.databinding.PreviewDocBinding

class PreviewDocument : AppCompatActivity() {
    private lateinit var binding: PreviewDocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreviewDocBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener { onBackPressed() }
    }
}

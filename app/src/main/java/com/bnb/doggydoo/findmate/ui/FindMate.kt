package com.bnb.doggydoo.findmate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.ActivityFindMateBinding
import com.bnb.doggydoo.findmate.adapter.FindMateAdapter

class FindMate : AppCompatActivity() {
    private lateinit var binding: ActivityFindMateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindMateBinding.inflate(layoutInflater)
        binding.oneCardLayout.setOnClickListener {
            binding.oneCardLayout.hide()
            binding.twoCardLayout.hide()
            binding.behind1.hide()
            binding.behind2.hide()
            binding.dogListLayout.show()
        }
        binding.dogListRv.adapter = FindMateAdapter(this)
        setContentView(binding.root)
        binding.close.setOnClickListener {
            binding.oneCardLayout.show()
            binding.twoCardLayout.show()
            binding.behind1.show()
            binding.behind2.show()
            binding.dogListLayout.hide()
        }
        /*binding.backButton.setOnClickListener {
            if (binding.dogListLayout.isVisible) {
                binding.oneCardLayout.show()
                binding.twoCardLayout.show()
                binding.behind1.show()
                binding.behind2.show()
                binding.dogListLayout.hide()
            } else
                finish()
        }*/
    }
}
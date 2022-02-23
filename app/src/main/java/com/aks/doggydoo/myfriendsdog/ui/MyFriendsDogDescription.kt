package com.aks.doggydoo.myfriendsdog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.ActivityMyFriendsDogDescriptionBinding

class MyFriendsDogDescription : AppCompatActivity() {
    private lateinit var binding: ActivityMyFriendsDogDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFriendsDogDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            supportFinishAfterTransition()
        }
    }
}
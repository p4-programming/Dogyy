package com.bnb.doggydoo.myfriendsdog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bnb.doggydoo.databinding.ActivityMyFriendsDogDescriptionBinding

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
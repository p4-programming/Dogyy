package com.bnb.doggydoo.myfrienddescription.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bnb.doggydoo.databinding.ActivityMyFriendDescriptionBinding
import com.bnb.doggydoo.myfrienddescription.adapter.FriendPetAdapter
import com.bnb.doggydoo.myfrienddescription.adapter.NewUploadAdapter
import com.bnb.doggydoo.myfriendsdog.ui.MyFriendsDogDescription

class MyFriendDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyFriendDescriptionBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFriendDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.friendPetRv.adapter = FriendPetAdapter(this) {view,petId->
            val imageViewPair =
                Pair(
                    view,
                    view.transitionName
                )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageViewPair
            )
            startActivity(
                Intent(this, MyFriendsDogDescription::class.java),
                options.toBundle()
            )
        }
        binding.newUpdatesRv.adapter = NewUploadAdapter(this)
        binding.backButton.setOnClickListener { supportFinishAfterTransition() }
    }
}
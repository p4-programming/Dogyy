package com.bnb.doggydoo.recievedrequestprofile.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.ActivityRequestedProfileBinding

class ReceivedRequestedProfile : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityRequestedProfileBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestedProfileBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener { supportFinishAfterTransition() }
        setContentView(binding.root)

        binding.menu.setOnClickListener {
            binding.menuItemLayout.show()
        }
        binding.viewProfile.setOnClickListener { binding.menuItemLayout.hide() }
        binding.viewFeed.setOnClickListener { binding.menuItemLayout.hide() }
        binding.block.setOnClickListener { binding.menuItemLayout.hide() }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }
}
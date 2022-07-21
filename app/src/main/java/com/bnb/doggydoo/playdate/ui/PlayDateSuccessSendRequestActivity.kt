package com.bnb.doggydoo.playdate.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.chatMessage.ChatMessageRequestActivity
import com.bnb.doggydoo.databinding.ActivityPlaydateSuccessSendreqBinding

class PlayDateSuccessSendRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaydateSuccessSendreqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaydateSuccessSendreqBinding.inflate(layoutInflater)

        binding.tvGoBack.setOnClickListener {
         finish()
        }
        binding.tvMessage.setOnClickListener {
           startActivity(Intent(this, ChatMessageRequestActivity::class.java).putExtra("from","request"))
        }
        setContentView(binding.root)
    }
}
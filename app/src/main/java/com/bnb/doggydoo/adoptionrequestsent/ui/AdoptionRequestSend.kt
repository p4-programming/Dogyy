package com.bnb.doggydoo.adoptionrequestsent.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.chatMessage.ChatMessageRequestActivity
import com.bnb.doggydoo.databinding.ActivityAdoptionRequestSendBinding

class AdoptionRequestSend : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionRequestSendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionRequestSendBinding.inflate(layoutInflater)
        getInit()
        setContentView(binding.root)
    }

    private fun getInit() {
        binding.goBack.setOnClickListener {
            finish()
        }


        binding.requestButton.setOnClickListener {
            startActivity(
                Intent(this, ChatMessageRequestActivity::class.java)
                    .putExtra("from", "request")
                    .putExtra("reqType","adoption")
            )
        }
    }
}
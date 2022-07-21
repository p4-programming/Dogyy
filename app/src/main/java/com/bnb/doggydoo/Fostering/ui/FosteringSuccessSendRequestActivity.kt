package com.bnb.doggydoo.fostering.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.chatMessage.ChatMessageRequestActivity
import com.bnb.doggydoo.databinding.ActivitySuccessRequestSentBinding
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.utils.CommonMethod

class FosteringSuccessSendRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessRequestSentBinding
    private var firebaseUid: String = ""
    private var userName: String = ""
    private var userImage: String = ""
    private var fromLocation: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessRequestSentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CommonMethod.makeTransparentStatusBar(window)

        getInit()
        binding.tvGoBack.setOnClickListener {
            finish()
        }
    }

    private fun getInit() {
        fromLocation = intent.getStringExtra("from").toString()

       // Toast.makeText(this, fromLocation, Toast.LENGTH_SHORT).show()

        if (fromLocation == "detail") {
            binding.tvMessage.text = "Request"
            binding.tvMessage.setOnClickListener {
                startActivity(
                    Intent(this, ChatMessageRequestActivity::class.java)
                        .putExtra("from", "request")
                        .putExtra("reqType", "fostering")
                )
            }
        } else {
            firebaseUid = intent.getStringExtra("uid").toString()
            userName = intent.getStringExtra("name").toString()
            userImage = intent.getStringExtra("userImage").toString()
            binding.tvMessage.text = "Message $userName"

            if (firebaseUid.isBlank()) {
                Toast.makeText(this, "User is not registered.", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvMessage.setOnClickListener {
                    startActivity(
                        Intent(this, ChatActivity::class.java)
                            .putExtra("name", userName)
                            .putExtra("uid", firebaseUid)
                            .putExtra("userImage", userImage)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )

                }

            }
        }
    }
}
package com.bnb.doggydoo.firebaseChat

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ChatImageFullActivity : AppCompatActivity() {
    private lateinit var userCharImage: ImageView
    private lateinit var chatName: TextView
    private lateinit var backIcon: ImageView
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_image_full)

        userCharImage = findViewById(R.id.ivChatImage)
        chatName = findViewById(R.id.tvChatUserName)
        backIcon = findViewById(R.id.ivBack)
        progress = findViewById(R.id.progressBar)

        backIcon.setOnClickListener {
            finish()
        }

        val userName = intent.getStringExtra("userName").toString()
        val userImage = intent.getStringExtra("image").toString()
        chatName.text = userName
        progress.setVisibility(View.VISIBLE)
        Picasso.get()
            .load(userImage)
            .into(userCharImage, object : Callback {
                override fun onSuccess() {
                    progress.setVisibility(View.GONE)
                }

                override fun onError(e: Exception?) {
                    progress.setVisibility(View.GONE)
                }
            })
    }
}
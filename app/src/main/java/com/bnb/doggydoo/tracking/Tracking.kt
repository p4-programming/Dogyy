package com.bnb.doggydoo.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bnb.doggydoo.R

class Tracking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
        val back=findViewById<ImageView>(R.id.backbutton)
        back.setOnClickListener {
            finish()
        }
    }
}
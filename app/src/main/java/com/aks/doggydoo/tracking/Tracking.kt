package com.aks.doggydoo.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.aks.doggydoo.R

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
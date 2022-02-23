package com.aks.doggydoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PaymentCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_card)
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener { finish() }
    }
}
package com.bnb.doggydoo.mydog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bnb.doggydoo.databinding.ActivityMarkResolvedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkResolved : AppCompatActivity() {

    private var binding:ActivityMarkResolvedBinding? = null
    private val bind get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkResolvedBinding.inflate(layoutInflater)
        setContentView(bind.root)

        binding!!.ivBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding!!.btn.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext,"Send",Toast.LENGTH_LONG).show()
            finish()
        })
    }
}
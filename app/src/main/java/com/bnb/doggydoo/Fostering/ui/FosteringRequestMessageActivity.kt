package com.bnb.doggydoo.fostering.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityRequestFosteringMeaasageBinding
import com.bnb.doggydoo.utils.CommonMethod

class FosteringRequestMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestFosteringMeaasageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestFosteringMeaasageBinding.inflate(layoutInflater)

        CommonMethod.makeTransparentStatusBar(window)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivRateUserDot.setOnClickListener{
           /* startActivity(Intent(this, RateUserActivity::class.java))*/
        }

        setContentView(binding.root)
    }
}
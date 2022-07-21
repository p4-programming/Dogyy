package com.bnb.doggydoo.dogsitrequestmsg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.ActivityDogsitRequestMassageBinding
import com.bnb.doggydoo.dogsitting.ui.PlaydateRequestBottomSheetFrag

class DogsitRequestMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDogsitRequestMassageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogsitRequestMassageBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivRateUserDot.setOnClickListener {
            binding.menuItemLayout.show()
        }
        binding.requestPlaydate.setOnClickListener {
            binding.menuItemLayout.hide()
            PlaydateRequestBottomSheetFrag().show(supportFragmentManager, "")
        }
        setContentView(binding.root)
    }

}
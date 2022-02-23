package com.aks.doggydoo.dogsitrequestmsg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.ActivityDogsitRequestMassageBinding
import com.aks.doggydoo.dogsitting.ui.PlaydateRequestBottomSheetFrag

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
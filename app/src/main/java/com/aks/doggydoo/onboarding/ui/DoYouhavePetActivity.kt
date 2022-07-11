package com.aks.doggydoo.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.ActivityDoyouHavepetBinding
import com.aks.doggydoo.login.ui.LoginActivity
import com.aks.doggydoo.utils.CommonMethod

class DoYouhavePetActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDoyouHavepetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoyouHavepetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.gifImageView.loadImageAsGif(this, R.raw.on_board_dog_next)

        CommonMethod.makeTransparentStatusBar(window)

        binding.lottie.addLottieOnCompositionLoadedListener { composition ->
            val startFrame = composition.startFrame
            val endFrame = composition.endFrame
            val end = endFrame - 1
            binding.lottie.setMinAndMaxFrame(1, end.toInt())

        }

        binding.button.setOnClickListener{
            startActivity(Intent(this, PetOnBoardingActivity::class.java))
            finish()
        }

        binding.noButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }
    override fun onBackPressed() {
        finish()
    }
}
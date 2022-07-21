package com.bnb.doggydoo.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.OnboardingBinding
import com.bnb.doggydoo.utils.CommonMethod
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "onBoardActTag"

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: OnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CommonMethod.makeTransparentStatusBar(window)
       // binding.gifImageView.loadImageAsGif(this, R.raw.onboard_look_up_down)


        binding.button.setOnClickListener{
            if (binding.etUserName.text.isEmpty()){
                binding.underlyingText.visibility = View.VISIBLE
            }else{
                binding.underlyingText.visibility = View.GONE
                startActivity(Intent(this, ChooseUserIdActivity::class.java)
                    .putExtra("name", binding.etUserName.text.toString()))
            }
        }

        binding.lottie.addLottieOnCompositionLoadedListener { composition ->
            val startFrame = composition.startFrame
            val endFrame = composition.endFrame
            val end = endFrame - 1
                binding.lottie.setMinAndMaxFrame(1, end.toInt())

        }

    }
    override fun onBackPressed() {
        finish()
    }
}
package com.aks.doggydoo.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.FragmentDogInfoBinding

class PetOnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: FragmentDogInfoBinding
    var petName: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDogInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.gifImageView.loadImageAsGif(this, R.raw.on_board_dog_next)

        binding.button.setOnClickListener{
            if (binding.ePetName.text.isEmpty()){
                binding.underlyingText.visibility = View.VISIBLE
            }else{
                binding.underlyingText.visibility = View.GONE
                petName = binding.ePetName.text.toString()
                startActivity(Intent(this, PetAgeActivity::class.java).
                putExtra("petname", petName))
            }

        }

        binding.ivBack.setOnClickListener {
            finish()
        }

    }
    override fun onBackPressed() {
        finish()
    }
}
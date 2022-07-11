package com.aks.doggydoo.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.ActivityPetageBinding
import com.aks.doggydoo.utils.CommonMethod

class PetAgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetageBinding
    var petName: String = ""
    var ageY: String = ""
    var ageM: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        CommonMethod.makeTransparentStatusBar(window)

    }

    private fun getInit() {
        petName = intent.getStringExtra("petname").toString()
        System.out.println("pet_name>>" + petName)

       // binding.gifImageView.loadImageAsGif(this, R.raw.on_board_dog_next)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.button.setOnClickListener {
            if (binding.tvPetAge.text.isEmpty()) {
                binding.underlyingText.visibility = View.VISIBLE
            } else if (ageY == "0" && ageM =="0"){
                binding.underlyingText.text = "Please enter a valid age!!"
                binding.underlyingText.visibility = View.VISIBLE
            }else {
                binding.underlyingText.visibility = View.GONE
                startActivity(
                    Intent(this, PetBreedActivity::class.java)
                        .putExtra("petname", petName)
                        .putExtra("petageyear", binding.fromNumber.value.toString())
                        .putExtra("petagemonth", binding.toNumber.value.toString())
                )
                finish()
            }

        }

        binding.llAge.visibility = View.GONE
        binding.button.visibility = View.VISIBLE
        binding.i.visibility = View.VISIBLE
        binding.ageLayout.visibility = View.VISIBLE

        binding.tvPetAge.setOnClickListener {
            binding.llAge.visibility = View.VISIBLE
            binding.button.visibility = View.INVISIBLE
            binding.i.visibility = View.INVISIBLE
            binding.ageLayout.visibility = View.INVISIBLE
            binding.underlyingText.visibility = View.GONE

            binding.fromNumber.maxValue = 20
            binding.fromNumber.minValue = 0
            binding.toNumber.maxValue = 12
            binding.toNumber.minValue = 0
        }


        binding.tvConfirm.setOnClickListener {
            binding.llAge.visibility = View.GONE
            binding.button.visibility = View.VISIBLE
            binding.i.visibility = View.VISIBLE
            binding.ageLayout.visibility = View.VISIBLE

            ageY = binding.fromNumber.value.toString()
            ageM = binding.toNumber.value.toString()

            binding.tvPetAge.text =
                ageY + " " + "Year" + " " + ageM + " " + "Month"
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
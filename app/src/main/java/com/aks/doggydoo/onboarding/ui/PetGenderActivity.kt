package com.aks.doggydoo.onboarding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.ActivityPetgenderBinding
import com.aks.doggydoo.utils.CommonMethod

class PetGenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetgenderBinding
    var petName: String =""
    var petAgeYear: String =""
    var petAgeMonth: String =""
    var petBreedId: String =""
    var petGender: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetgenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()

        CommonMethod.makeTransparentStatusBar(window)
    }

    private fun getInit() {
        petName = intent.getStringExtra("petname").toString()
        petAgeYear = intent.getStringExtra("petageyear").toString()
        petAgeMonth = intent.getStringExtra("petagemonth").toString()
        petBreedId = intent.getStringExtra("petbreedid").toString()

        //binding.gifImageView.loadImageAsGif(this, R.raw.on_board_dog_next)

        binding.button.setOnClickListener{
            if(petGender==""){
                binding.underlyingText.visibility = View.VISIBLE
            }else{
                binding.underlyingText.visibility = View.GONE
                startActivity(
                    Intent(this, PetImageUploadActivity::class.java)
                    .putExtra("pet_name", petName)
                    .putExtra("pet_ageyear",petAgeYear)
                    .putExtra("pet_agemonth",petAgeMonth)
                    .putExtra("pet_breedid",petBreedId)
                    .putExtra("pet_gender", petGender)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        binding.ivback.setOnClickListener {
            finish()
        }

        binding.male.setOnClickListener {
            binding.male.backgroundTintList =
                resources.getColorStateList(R.color.male_female_dog_gender)
            binding.female.backgroundTintList =
                resources.getColorStateList(R.color.white)
            petGender = "Male"
        }

        binding.female.setOnClickListener {
            binding.male.backgroundTintList =
                resources.getColorStateList(R.color.white)
            binding.female.backgroundTintList =
                resources.getColorStateList(R.color.male_female_dog_gender)
            petGender = "Female"
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
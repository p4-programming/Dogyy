package com.bnb.doggydoo.adoptdogdetails.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.adoptdogdetails.datasource.model.AdoptionDogList
import com.bnb.doggydoo.adoptdogdetails.viewmodel.AdoptionDogDetailViewModel
import com.bnb.doggydoo.adoptionrequestsent.ui.AdoptionRequestSend
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.AdoptDogDetailBinding
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.bnb.doggydoo.utils.network.ApiConstant.PROFILE_IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptDogDetailActivity : AppCompatActivity() {
    private var adoptionId = ""
    private lateinit var binding: AdoptDogDetailBinding
    private lateinit var adoptionDogDetailViewModel: AdoptionDogDetailViewModel
    private var receiveId: String = ""
    private var petId: String = ""
    private var petName: String =""
    private var petImage: String = ""
    private var firebaseUid: String =""
    private var userName: String =""
    private var userImage: String =""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdoptDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        adoptionDetailAPI()

    }

    private fun getInit() {
        adoptionDogDetailViewModel = ViewModelProvider(this).get(AdoptionDogDetailViewModel::class.java)
        adoptionId = intent.getStringExtra("adoptId").toString()
        binding.backButton.setOnClickListener {
            supportFinishAfterTransition()
        }
        if (intent.hasExtra("from")) {
            binding.adoptDog.text = "Send Mating Request"
        } else {
            binding.adoptDog.text = "Adopt"
        }
        binding.adoptDog.setOnClickListener {
            callSendAdoptionAPI()
        }

        binding.navIcon.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                petName + " " + "looking for a home and is up for adoption on DoggyDoo" + "\n" + petImage
            )
            startActivity(Intent.createChooser(intent, "Share with:"))
        }

        binding.homeImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java)
                .putExtra("viewuserid", receiveId)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.msgButton.setOnClickListener {
            if (firebaseUid.isBlank()){
                Toast.makeText(this, "User is not registered.", Toast.LENGTH_SHORT).show()
            }else{
                startActivity(
                    Intent(this, ChatActivity::class.java)
                        .putExtra("name", userName)
                        .putExtra("uid", firebaseUid)
                        .putExtra("userImage", userImage)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )

            }
        }
    }

    private fun callSendAdoptionAPI() {
        adoptionDogDetailViewModel.sendAdoption(MyApp.getSharedPref().userId, receiveId, petId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }
                        startActivity(Intent(this, AdoptionRequestSend::class.java))
                        finish()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun adoptionDetailAPI() {
        adoptionDogDetailViewModel.getAdoptionDogDetailList(
            adoptionId,
            MyApp.getSharedPref().userId
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == "0") {
                            binding.rlMain.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                            //  it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }
                        if (it.data.adoptionList.size > 0) {
                            setDataInUI(it.data.adoptionList)
                        } else {
                            binding.rlMain.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.rlMain.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                        // it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(adoptionList: List<AdoptionDogList>) {
        petName = adoptionList[0].name
        this.receiveId = adoptionList[0].user.user_id.toString()
        this.petId = adoptionList[0].pet_id
        binding.name.text = adoptionList[0].name
        binding.breedAge.text =
            "${adoptionList[0].breed}, ${adoptionList[0].age} Years ${adoptionList[0].pet_age_month} Months old"
        binding.descriptionText.text = adoptionList[0].description
        binding.address.text = adoptionList[0].user.address
        binding.userName.text = adoptionList[0].user.name

        if (adoptionList[0].pic.size > 0){
            petImage = ApiConstant.PET_IMAGE_BASE_URL + adoptionList[0].pic[0]
            if (adoptionList[0].pic[0] == "user.png")
                binding.dogImage.setImageResource(R.drawable.dummy_pet)
            else
                binding.dogImage.loadImageFromString(
                    this,
                    ApiConstant.PET_IMAGE_BASE_URL + adoptionList[0].pic[0]
                )
        }else{
            binding.dogImage.setImageResource(R.drawable.dummy_pet)
        }
        binding.homeImage.loadImageFromString(
            this,
            PROFILE_IMAGE_BASE_URL + adoptionList[0].user.photo
        )

        binding.adoptDog.text = "Adopt" + " " + adoptionList[0].name

        firebaseUid = adoptionList[0].user.userUid
        userName = adoptionList[0].user.name
        userImage = adoptionList[0].user.photo
    }
}
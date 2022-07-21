package com.bnb.doggydoo.dogsitting.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActvityDogsittingDetailBinding
import com.bnb.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogSittingDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActvityDogsittingDetailBinding
    private lateinit var myDogViewModel: MyDogViewModel
    private var petId: String = ""
    private var receiveId: String = ""
    private var heroName: String =""
    private var firebaseUid: String =""
    private var userName: String =""
    private var userImage: String =""
    private lateinit var dogsittingmodel: DogsittingModel
    private var startDate:String = ""
    private var endDate:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActvityDogsittingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        callGetDogDescriptionAPI()
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)

        petId = intent.getStringExtra("petid").toString()
        binding.backButton.setOnClickListener {
            supportFinishAfterTransition()
        }

        binding.tvSendDogRequest.setOnClickListener {
            if (receiveId.isNotEmpty() && petId.isNotEmpty()){
                sendDogsitReqAPI(receiveId,petId)
            }else{
                CommonMethod.showSnack(binding.parent,"Unable to send request.")
            }

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

    private fun callGetDogDescriptionAPI() {
        System.out.println("ped id>>" + petId.toString())
        System.out.println("ped id>>" + MyApp.getSharedPref().userId)
        myDogViewModel.getPetDescriptionData(petId, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        setDataInUI(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(data: PetDescriptionResponse?) {
        if (data!= null){
            this.receiveId = data.userdetail.id
            this.petId = data.petdetail.id
            binding.tvName.text = data.petdetail.pet_name
            binding.tvBreedAge.text = data.petdetail.breed +","+ "${data.petdetail.pet_age} Year"+" "+ "${data.petdetail.pet_age_month} Month old"
            binding.tvDescription.text = data.petdetail.pet_description
            if (data.petdetail.pet_image == "user.png") {
                binding.ivDog.setImageResource(R.drawable.dummy_pet)
            } else {
                if (data.petImage.size > 0){
                    binding.ivDog.loadImageFromString(
                        this,
                        ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
                    )
                }else{
                    binding.ivDog.setImageResource(R.drawable.dummy_pet)
                }

            }

            binding.homeImage.loadImageFromString(
                this@DogSittingDetailActivity,
                ApiConstant.PROFILE_IMAGE_BASE_URL + data.userdetail.profile_pic
            )
            binding.tvUserName.text =  data.userdetail.uname
            binding.tvAddress.text =  data.userdetail.address
            binding.tvKm.text =  data.userdetail.km+" "+"Km"
            heroName = data.userdetail.uname

            firebaseUid = data.userdetail.userUid
            userName = data.userdetail.uname
            userImage = data.userdetail.profile_pic


            if (MyApp.getSharedPref().userId.equals(data.userdetail.id)){
                binding.msgButton.visibility = View.GONE
                binding.tvSendDogRequest.visibility = View.GONE
            }else{
                binding.msgButton.visibility = View.VISIBLE
                binding.tvSendDogRequest.visibility = View.VISIBLE
            }
            binding.tvDate.text = data.petdetail.start_date + " - "+ data.petdetail.end_date
            binding.tvTime.text = data.petdetail.start_time + " - "+ data.petdetail.end_time
        }

    }

    private fun sendDogsitReqAPI(receiveId: String, petId: String) {
        dogsittingmodel.sendAllDogsitReq(
            MyApp.getSharedPref().userId,
            receiveId,
            petId,
            "",
            ""
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                            it.data.responseMessage.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }

                        navigateTo(it.message)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun navigateTo(message: String?) {
        CommonMethod.showSnack(binding.root, message)
        startActivity(Intent(this@DogSittingDetailActivity, SuccessSendRequestActivity::class.java)
            .putExtra("from", "dog")
            .putExtra("heroname", heroName)
            .putExtra("heroId", receiveId))
        finish()
    }
}
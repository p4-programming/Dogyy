package com.aks.doggydoo.fostering.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityFosteringDetailBinding
import com.aks.doggydoo.firebaseChat.ChatActivity
import com.aks.doggydoo.fostering.datasource.model.fosteringDetail
import com.aks.doggydoo.fostering.viewmodel.FosteringModel
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FosteringDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFosteringDetailBinding
    private lateinit var fosteringmodel: FosteringModel
    private var fosterId: String = ""
    private var receivedId: String = ""
    private var firebaseUid: String = ""
    private var userName: String = ""
    private var userImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFosteringDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callFosterDetailAPI()
    }

    private fun getInit() {
        fosteringmodel = ViewModelProvider(this).get(FosteringModel::class.java)
        fosterId = intent.getStringExtra("fostertId").toString()
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.tvSendFosteringRequest.setOnClickListener {
            callFosterReqAPI()
        }

        binding.homeImage.setOnClickListener {
            startActivity(
                Intent(this, UserProfileActivity::class.java)
                    .putExtra("viewuserid", receivedId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.msgButton.setOnClickListener {
            if (firebaseUid.isBlank()) {
                Toast.makeText(this, "User is not registered.", Toast.LENGTH_SHORT).show()
            } else {
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

    private fun callFosterDetailAPI() {
        fosteringmodel.getFosteringDetail(fosterId, MyApp.getSharedPref().userId)
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
                                binding.root
                            )
                            return@Observer
                        }
                        setDataInUI(it.data.fosteringDetaillist)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun setDataInUI(fosteringDetaillist: fosteringDetail) {
        receivedId = fosteringDetaillist.user_id
        binding.dogImage.loadImageFromString(
            this@FosteringDetailActivity,
            ApiConstant.PET_IMAGE_BASE_URL + fosteringDetaillist.picList[0]
        )
        binding.name.text = fosteringDetaillist.name
        binding.breedAge.text =
            fosteringDetaillist.breed + " " + "," + fosteringDetaillist.age + " Years Old"
        binding.descriptionText.text = fosteringDetaillist.description
        binding.homeImage.loadImageFromString(
            this@FosteringDetailActivity,
            ApiConstant.PROFILE_IMAGE_BASE_URL + fosteringDetaillist.user_photo
        )
        binding.userName.text = fosteringDetaillist.user_name
        binding.address.text = fosteringDetaillist.address
        binding.tvDistance.text = fosteringDetaillist.km + " " + "Km away"

        firebaseUid = fosteringDetaillist.userUid
        userName = fosteringDetaillist.user_name
        userImage = fosteringDetaillist.user_photo

        if (MyApp.getSharedPref().userId.equals(fosteringDetaillist.user_id)){
            binding.msgButton.visibility = View.GONE
            binding.tvSendFosteringRequest.visibility = View.GONE
        }else{
            binding.msgButton.visibility = View.VISIBLE
            binding.tvSendFosteringRequest.visibility = View.VISIBLE
        }
    }

    private fun callFosterReqAPI() {
        fosteringmodel.sendFosteringRequest("",MyApp.getSharedPref().userId,receivedId,fosterId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        //CommonMethod.showSnack(binding.root, "Request sent successfully")
                        if (it.data!!.responseCode == ("0")) {
                            it.data.responseMessage.snack(
                                Color.RED,
                                binding.root
                            )
                            return@Observer
                        }
                        startActivity(Intent(this, FosteringSuccessSendRequestActivity::class.java)
                            .putExtra("from","detail")
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.root)
                    }
                }
            })
    }
}
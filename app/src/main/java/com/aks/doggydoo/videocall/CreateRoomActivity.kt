package com.aks.doggydoo.videocall

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityCreateRoomBinding
import com.aks.doggydoo.firebaseChat.Notification.*
import com.aks.doggydoo.homemodule.viewmodel.HomeViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRoomActivity : AppCompatActivity() {
    private var _binding: ActivityCreateRoomBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private var roomId: String = ""
    private var refNo: String = ""
    private var tokenIs: String = ""
    private var type:String =""
    private var clickedUserId: String = ""
    private var clickedPetId:String =""
    private var callingUserImage:String =""
    private var callingUserName:String =""
    private var from:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calling.hide()
        binding.call.setOnClickListener {
            binding.calling.show()
            generateRoom()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (intent != null){
            clickedUserId = intent.getStringExtra("clickedUserID").toString()
            clickedPetId = intent.getStringExtra("playdateId").toString()
            callingUserImage = intent.getStringExtra("userImage").toString()
            callingUserName = intent.getStringExtra("name").toString()
            from = intent.getStringExtra("from").toString()
        }


        binding.tvUserName.setText(callingUserName)
        binding.ivUser.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
        )

        binding.imageView.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + callingUserImage
        )
    }

    private fun generateRoom() {
        System.out.println("data is>>" + clickedPetId)
        homeViewModel.getToken(clickedPetId, from)
            .observe(this@CreateRoomActivity, androidx.lifecycle.Observer {
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
                        refNo = it.data.refrence_id
                        roomId = it.data.room_id
                        type = it.data.type
                        generateToken(roomId, refNo, clickedUserId,type)

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }


    private fun generateToken(roomId: String, refNo: String, clickedUserId: String, type: String) {
        System.out.println("data is>>" + MyApp.getSharedPref().userName)
        System.out.println("data is>>" +refNo)
        System.out.println("data is>>" + roomId)
        System.out.println("data is>>" + clickedUserId)
        System.out.println("data is>>" + type)
        homeViewModel.getGenToken(
           MyApp.getSharedPref().userName,
            refNo,
            roomId,
            clickedUserId,
            type,
            MyApp.getSharedPref().userId
        )
            .observe(this@CreateRoomActivity, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }else{
                            tokenIs = it.data.token
                            successNavigation(tokenIs,type)
                        }


                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun successNavigation(tokenIs: String, type: String) {
        startActivity(
            Intent(
                this, VideoConferenceActivity::class.java
            )
                .putExtra("token", tokenIs)
                .putExtra("name", "user")
                .putExtra("request_type", type)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

}
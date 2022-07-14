package com.aks.doggydoo.playdate.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.aks.doggydoo.chatMessage.ChatMessageRequestActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityPlaydateDetailBinding
import com.aks.doggydoo.firebaseChat.ChatActivity
import com.aks.doggydoo.homemodule.viewmodel.HomeViewModel
import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.Petdetail
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.playdate.viewmodel.PlayDateViewModel
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import com.aks.doggydoo.videocall.CreateRoomActivity
import com.aks.doggydoo.videocall.VideoConferenceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayDateDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaydateDetailBinding
    private val playDateViewModel: PlayDateViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var receiveId: String = ""
    private var petId: String = ""
    private var firebaseUid: String = ""
    private var userName: String = ""
    private var userImage: String = ""
    private var from: String = ""
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val requestcode = 1
    private var playdateReqId: String = ""
    private var playReqdate: String = ""
    private var playDateType: String = ""
    private var requestId: String = ""


    private var roomId: String = ""
    private var refNo: String = ""
    private var tokenIs: String = ""
    private var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaydateDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        petPlayDetailAPI()
    }

    private fun getInit() {
        petId = intent.getStringExtra("pet_id").toString()
        from = intent.getStringExtra("from").toString()
        requestId = intent.getStringExtra("request_id").toString()

        //Toast.makeText(this, from, Toast.LENGTH_SHORT).show()


        binding.backButton.setOnClickListener {
            finish()
        }
        binding.tvSelectTimeDate.setOnClickListener {
            startActivity(
                Intent(this, SelectTimeDateActivity::class.java)
                    .putExtra("receiveId", receiveId)
                    .putExtra("petId", petId)
                    .putExtra("from", "other")
            )
            finish()
        }

        binding.homeImage.setOnClickListener {
            startActivity(
                Intent(this, UserProfileActivity::class.java)
                    .putExtra("viewuserid", receiveId)
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


        binding.tvCall.setOnClickListener {
            if (playDateType == "online") {
                if (!isPermissionGranted()) {
                    askPermissions()
                } else {
                    generateRoom()
                    /* startActivity(
                         Intent(this, CreateRoomActivity::class.java)
                             .putExtra("from", "1")
                             .putExtra("clickedUserID", receiveId)
                             .putExtra("playdateId", playdateReqId)
                             .putExtra("userImage", userImage)
                             .putExtra("name", userName)
                             .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                     )*/
                }

            }
        }
    }

    private fun askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestcode)
    }

    private fun isPermissionGranted(): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }

        return true
    }

    private fun petPlayDetailAPI() {
        System.out.println("data is>>" + petId)
        System.out.println("data is>>" + MyApp.getSharedPref().userId)

        playDateViewModel.getPetDescriptionData(MyApp.getSharedPref().userId, petId)
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
                        if (!it.data.petdetail.isEmpty())
                            setDataInUI(it.data.petdetail)

                        if (it.data.playdate.size > 0) {
                            binding.playDate.show()
                            playdateReqId = it.data.playdate[0].request_id
                            playReqdate = it.data.playdate[0].day
                            binding.playDate.text = playReqdate
                            playDateType = it.data.playdate[0].date_mode
                            if (playDateType != "online") {
                                binding.tvCall.text = "In Person Meeting"
                            }
                        } else {
                            binding.playDate.hide()
                        }
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun setDataInUI(petDetail: List<Petdetail>) {
        if (from == "upcoming") {
            binding.tvSelectTimeDate.hide()
            binding.tvCall.show()
        } else {
            binding.tvSelectTimeDate.show()
            binding.tvCall.hide()
        }

        if (MyApp.getSharedPref().userId.equals(petDetail[0].user_id)) {
            binding.msgButton.visibility = View.GONE
            // binding.tvSelectTimeDate.visibility = View.GONE
        } else {
            binding.msgButton.visibility = View.VISIBLE
            //  binding.tvSelectTimeDate.visibility = View.VISIBLE
        }


        firebaseUid = petDetail[0].userUid
        userName = petDetail[0].uname
        userImage = petDetail[0].profile_pic

        receiveId = petDetail[0].user_id
        petId = petDetail[0].id
        binding.name.text = petDetail[0].pet_name + ", " + petDetail[0].pet_gender
        binding.breedAge.text =
            "${petDetail[0].breed}, ${petDetail[0].pet_age} ${petDetail[0].pet_age_type} years Old"
        binding.descriptionText.text = petDetail[0].pet_description
        binding.address.text = petDetail[0].address
        binding.userName.text = petDetail[0].uname
        binding.tvDistance.text = petDetail[0].km + " " + "Km away"
        binding.dogImage.loadImageFromString(
            this,
            ApiConstant.PET_IMAGE_BASE_URL + petDetail[0].pet_image
        )
        binding.homeImage.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + petDetail[0].profile_pic
        )


        /*Toast.makeText(this, petDetail[0].user_id, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, MyApp.getSharedPref().userId, Toast.LENGTH_SHORT).show()*/


    }


    /////////////////////////////////////////for calling ////////////////////////////

    private fun generateRoom() {
        homeViewModel.getToken(playdateReqId, from)
            .observe(this, androidx.lifecycle.Observer {
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
                        generateToken(roomId, refNo, playdateReqId, type)

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
        System.out.println("data is>>" + refNo)
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
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        } else {
                            tokenIs = it.data.token
                            successNavigation(tokenIs, type)
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
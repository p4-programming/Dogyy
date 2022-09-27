package com.bnb.doggydoo.myprofile.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityUserprofileBinding
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.mydog.ui.PetProfileActivity
import com.bnb.doggydoo.myfrienddescription.adapter.FriendPetAdapter
import com.bnb.doggydoo.myfrienddescription.adapter.NewUploadAdapter
import com.bnb.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.bnb.doggydoo.myprofile.viewmodel.MyProfileViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserprofileBinding
    private lateinit var adapter: FriendPetAdapter
    private lateinit var newUploadAdapter: NewUploadAdapter
    //  private val myProfileViewModel: MyProfileViewModel by viewModels()
    // private val homeViewModel: HomeViewModel by viewModels()

    var userId: String = ""
    var profileId: String = ""
    private var firebaseUid: String = ""
    private var userName: String = ""
    private var userImage: String = ""
    private var requestedId: String = ""
    private var from: String = ""
    private lateinit var myProfileViewModel: MyProfileViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var chat: ChatActivity

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getMyProfileInfoAPI()
    }

    private fun getInit() {
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        userId = intent.getStringExtra("viewuserid").toString()

        from = if (intent.hasExtra("from")) {
            intent.getStringExtra("from").toString()
        } else {
            ""
        }

        if (from == "map") {
            binding.tvUserAllFriends.hide()
        } else {
            // binding.tvUserAllFriends.show() // changed on 22/6/2022
            binding.tvUserAllFriends.hide()
        }

        //Toast.makeText(this,"userid=="+userId, Toast.LENGTH_SHORT).show()

        if (userId.isEmpty()) {
            if (MyApp.getSharedPref().userId == userId) {
                binding.llRequest.hide()
            } else {
                binding.llRequest.show()
            }
        } else {
            System.out.println("No valid user found.")
        }

        adapter = FriendPetAdapter(this) { view, petId ->
            val imageViewPair =
                Pair(
                    view,
                    view.transitionName
                )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageViewPair
            )
            startActivity(
                Intent(this, PetProfileActivity::class.java)
                    .putExtra("petId", petId),
                options.toBundle()
            )
            finish()
        }
        newUploadAdapter = NewUploadAdapter(this)
        binding.myPetRv.adapter = adapter
        binding.myNewUpdatesRv.adapter = newUploadAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.tvSendFriendReq.setOnClickListener {
            if (binding.tvSendFriendReq.text == "Send Request") {
                sendFriendReqAPI()
            } else if (binding.tvSendFriendReq.text == "Accept Request") {
                callAcceptOrRejectAPI()
            } else if (binding.tvSendFriendReq.text == "Request Sent") {
                Toast.makeText(this, "Request already sent.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You guys are friends.", Toast.LENGTH_SHORT).show()
            }

        }



        binding.tvSendMessage.setOnClickListener {
            if (binding.tvSendFriendReq.text == "Friend") {
                if (firebaseUid.isBlank()) {
                    Toast.makeText(this, "User is not registered.", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(
                        Intent(this, ChatActivity::class.java)
                            .putExtra("name", userName)
                            .putExtra("uid", firebaseUid)
                            .putExtra("viewuserid",userId)
                            .putExtra("userImage", userImage)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    Log.d("Deepak","UID : $userId")
                }
            } else {
                Toast.makeText(this, "You are not friends yet.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.userMenu.setOnClickListener {
            if (binding.tvSendFriendReq.text == "Friend") {
                blockDialog()
            } else {
                Toast.makeText(this, "Can not block the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun blockDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_usermenu)

        val tvUnfriend = dialog.findViewById<View>(R.id.tvUnfriend) as TextView
        val tvReport = dialog.findViewById<View>(R.id.tvReport) as TextView
        val ivCancel = dialog.findViewById<View>(R.id.ivCancel) as ImageView

        tvUnfriend.setOnClickListener {
            blockReportAPI("5")
            dialog.dismiss()
        }

        tvReport.setOnClickListener {
            blockReportAPI("6")
            dialog.dismiss()
        }

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getMyProfileInfoAPI() {
        myProfileViewModel.getMyProfileInfoData(userId, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        if (it.data.mypet.isEmpty()) {
                            binding.noPets.show()
                        }
                        if (it.data.newupload.isEmpty()) {
                            binding.noUploads.show()
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
    private fun setDataInUI(profileResponse: MyProfileResponse) {
        profileId = profileResponse.userdetails[0].id
        requestedId = profileResponse.requestid
        firebaseUid = profileResponse.userdetails[0].userUid
        userImage = profileResponse.userdetails[0].profile_pic
        userName = profileResponse.userdetails[0].uname
        binding.friendNameAndAge.text = profileResponse.userdetails[0].uname
        binding.detail.text = profileResponse.userdetails[0].description
        //binding.dogSittingCheck.isChecked = profileResponse.userdetails[0].dogsitter == "yes"
        //binding.fosterCheck.isChecked = profileResponse.userdetails[0].fostering == "yes"
        //binding.adoptCheck.isChecked = profileResponse.userdetails[0].adoption == "yes"
        binding.tvUserName.text = profileResponse.userdetails[0].uname + "'s Friends"
        binding.tvUserTotalFriends.text = profileResponse.friend_count + " Friends"

        if (profileResponse.check_friend == "2") {
            binding.tvSendFriendReq.text = "Request Sent"
            binding.userMenu.visibility = View.GONE

        } else if (profileResponse.check_friend == "1") {
            binding.tvSendFriendReq.text = "Friend"
            binding.userMenu.visibility = View.VISIBLE

        } else if (profileResponse.check_friend == "3") {
            binding.tvSendFriendReq.text = "Accept Request"
            binding.userMenu.visibility = View.GONE

        } else {
            if (MyApp.getSharedPref().userId == profileResponse.userdetails[0].id) {
                binding.tvSendFriendReq.text = "My Account"
                binding.tvSendFriendReq.isEnabled = false
            } else {
                binding.tvSendFriendReq.text = "Send Request"
                binding.userMenu.visibility = View.GONE
            }
        }

        /*  binding.tvSendFriendReq.setOnClickListener {
              if (binding.tvSendFriendReq.text == "Accept Request") {
                  callAcceptOrRejectAPI()
              }
          }*/


        if (profileResponse.userdetails[0].profile_pic == "user.png") {
            binding.userImage.setImageResource(R.drawable.dummy_user)
        } else {
            binding.userImage.loadImageFromString(
                this,
                ApiConstant.PROFILE_IMAGE_BASE_URL + profileResponse.userdetails[0].profile_pic
            )
            binding.userImage.show()
        }

        adapter.submitList(profileResponse.mypet)
        newUploadAdapter.submitList(profileResponse.newupload)
        MyApp.getSharedPref().userEmail = profileResponse.userdetails[0].email


        if (profileResponse.userdetails[0].adoptionKing != "1") {
            binding.ivAdoptionKing.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].rescueHero != "1") {
            binding.ivRescueHero.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].dogsitChamp != "1") {
            binding.ivDogsitterHero.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }
    }

    private fun sendFriendReqAPI() {
        myProfileViewModel.friendReqInfoData(MyApp.getSharedPref().userId, profileId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        "Request sent successfully".snack(Color.RED, binding.parent)

                        if (it.data!!.responseCode == "0") {
                            return@Observer
                        }
                        getMyProfileInfoAPI()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun blockReportAPI(actionType: String) {
        myProfileViewModel.blockReportUserData(profileId, actionType, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.responseMessage?.snack(Color.BLACK, binding.parent)

                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        getMyProfileInfoAPI()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun callAcceptOrRejectAPI() {
        System.out.println("blockdata>>" + profileId)

        homeViewModel.AcceptRejectFriend(requestedId, "1")
            .observe(this,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            CommonMethod.showSnack(binding.root, "Request accepted.")
                            getMyProfileInfoAPI()

                            if (it.data!!.responseCode == "0") {
                                return@Observer
                            }

                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }
}
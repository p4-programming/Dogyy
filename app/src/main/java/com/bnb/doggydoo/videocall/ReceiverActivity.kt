package com.bnb.doggydoo.videocall

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityReceiverBinding
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiverActivity : AppCompatActivity() {
    private var _binding: ActivityReceiverBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private var refNo: String = ""
    private var roomId: String = ""
    private var type: String = ""
    private var callerImage:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            refNo = intent.getStringExtra("refId").toString()
            roomId = intent.getStringExtra("roomId").toString()
            type = intent.getStringExtra("type").toString()
            callerImage = intent.getStringExtra("userImage").toString()
        }

        binding.ivUser.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
        )
        binding.imageView.loadImageFromString(
            this,
            ApiConstant.PROFILE_IMAGE_BASE_URL + callerImage
        )

        binding.call.setOnClickListener {
            getToken(roomId, refNo, type)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getToken(roomId: String, refNo: String, type: String) {
        System.out.println("data is>>" + MyApp.getSharedPref().userName)
        System.out.println("data is>>" + refNo)
        System.out.println("data is>>" + roomId)
        System.out.println("data is>>" + MyApp.getSharedPref().userId)
        System.out.println("data is>>" + type)
        homeViewModel.getGenToken(
            MyApp.getSharedPref().userName,
            refNo,
            roomId,
            "0",
            type,
            MyApp.getSharedPref().userId
        )
            .observe(this@ReceiverActivity, androidx.lifecycle.Observer {
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
                        successNavigation(it.data.token, type)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun successNavigation(tokenIs: String, type: String) {
        //Toast.makeText(this, tokenIs, Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(
                this, VideoConferenceActivity::class.java
            )
                .putExtra("token", tokenIs)
                .putExtra("name", "user")
                .putExtra("request_type", type)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}
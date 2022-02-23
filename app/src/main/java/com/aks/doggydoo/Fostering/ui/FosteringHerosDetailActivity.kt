package com.aks.doggydoo.fostering.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityFosteringHerosDetailsBinding
import com.aks.doggydoo.dogsitting.adapter.PetAdapter
import com.aks.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.aks.doggydoo.fostering.viewmodel.FosteringModel
import com.aks.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.myprofile.viewmodel.MyProfileViewModel
import com.aks.doggydoo.newsfeed.util.RecyclerTouchListener
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FosteringHerosDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFosteringHerosDetailsBinding
    //initialized view model here
    private lateinit var myProfileViewModel: MyProfileViewModel
    var fosterHeroId: String =""
    private var firebaseUid:String =""
    private var userName:String =""
    private var userImage:String =""
    private lateinit var dogsittingmodel: DogsittingModel
    private lateinit var adapterpet: PetAdapter
    private var breedIdList = ArrayList<String>()
    private var valuePetId: String = ""
    private var clickedItem:String ="0"
    private lateinit var fosteringmodel: FosteringModel
    private var receivedId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFosteringHerosDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getInit()
        getMyProfileInfoAPI()
    }

    private fun getInit() {
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)
        fosteringmodel = ViewModelProvider(this).get(FosteringModel::class.java)
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)

        fosterHeroId = intent.getStringExtra("fostertHeroId").toString()
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.llPet.visibility = View.GONE
        binding.tvSendFosteringRequest.setOnClickListener {
            binding.llPet.visibility = View.VISIBLE
            getAllDogAPI()
        }

        adapterpet = PetAdapter(this)
        binding.rvPet.adapter = adapterpet
        binding.rvPet.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.rvPet,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        clickedItem ="1"
                        valuePetId = breedIdList[position]

                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        binding.tvConfirm.setOnClickListener {
            callFosterReqAPI()
        }

        binding.friendNameAndAge.setOnClickListener {
            startActivity(
            Intent(this, UserProfileActivity::class.java)
                .putExtra("viewuserid", receivedId))
        }
       /* binding.msgButton.setOnClickListener {
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
        }*/
    }

    private fun getAllDogAPI() {
        dogsittingmodel.getAllDogList(
            MyApp.getSharedPref().userId)
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

                        //set adapter
                        for (category in it.data.alldoglist) {
                            breedIdList.add(category.id)
                        }
                        adapterpet.submitList(it.data.alldoglist)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun getMyProfileInfoAPI() {
        System.out.println("user id>>" + fosterHeroId)
        myProfileViewModel.getMyProfileInfoData(fosterHeroId,"")
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
                        setDataInUI(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun setDataInUI(profileResponse: MyProfileResponse) {
        receivedId = profileResponse.userdetails[0].id
        firebaseUid = profileResponse.userdetails[0].userUid
        userName = profileResponse.userdetails[0].uname
        userImage = profileResponse.userdetails[0].profile_pic
        binding.friendNameAndAge.text = "${profileResponse.userdetails[0].uname}," +" "+ "${profileResponse.userdetails[0].uage}"
        binding.detail.text = profileResponse.userdetails[0].description
        if(profileResponse.userdetails[0].dogsitter == "yes"){
            binding.req.text = "Dogsitter"
        }
        if(profileResponse.userdetails[0].fostering == "yes"){
            binding.req.text = "Foster"
        }

        if (profileResponse.userdetails[0].profile_pic == "user.png") {
            binding.userImage.setImageResource(R.drawable.dummy_user)
        } else {
            binding.userImage.loadImageFromString(
                this,
                ApiConstant.PROFILE_IMAGE_BASE_URL + profileResponse.userdetails[0].profile_pic
            )
        }
    }

    private fun callFosterReqAPI() {
        fosteringmodel.sendFosteringRequest("0",MyApp.getSharedPref().userId,receivedId,valuePetId)
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
                        binding.llPet.visibility = View.GONE
                        startActivity(
                            Intent(this, FosteringSuccessSendRequestActivity::class.java)
                                .putExtra("name", userName)
                                .putExtra("uid", firebaseUid)
                                .putExtra("userImage", userImage)
                                .putExtra("from","fostered")
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
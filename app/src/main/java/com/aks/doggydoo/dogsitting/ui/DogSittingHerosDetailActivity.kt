package com.aks.doggydoo.dogsitting.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityDogsittingHerosDetailsBinding
import com.aks.doggydoo.dogsitting.adapter.PetAdapter
import com.aks.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.aks.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.myprofile.viewmodel.MyProfileViewModel
import com.aks.doggydoo.newsfeed.util.RecyclerTouchListener
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogSittingHerosDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDogsittingHerosDetailsBinding
    private lateinit var myProfileViewModel: MyProfileViewModel
    var dogsitHeroId: String =""
    private lateinit var dogsittingmodel: DogsittingModel
    private lateinit var adapterpet: PetAdapter
    private var breedIdList = ArrayList<String>()
    private var valuePetId: String = ""
    private var clickedItem:String ="0"
    private var heroName: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogsittingHerosDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        getMyProfileInfoAPI()
    }

    private fun getInit() {
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        dogsitHeroId = intent.getStringExtra("dogsitHeroId").toString()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.llPet.visibility = View.GONE
        binding.chooseDateTime.setOnClickListener {
            binding.llPet.visibility = View.VISIBLE
            getAllDogAPI()
        }

        binding.tvConfirm.setOnClickListener {
            if (clickedItem =="1"){
                sendDogsitReqAPI()
            }else{
                Toast.makeText(this, "Please select a pet.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.friendNameAndAge.setOnClickListener {
            startActivity(
                Intent(this, UserProfileActivity::class.java)
                    .putExtra("viewuserid", dogsitHeroId))
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
                        valuePetId = breedIdList.get(position)

                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }

    private fun getMyProfileInfoAPI() {
        System.out.println("user id>>" + dogsitHeroId)
        myProfileViewModel.getMyProfileInfoData(dogsitHeroId,"")
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

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(profileResponse: MyProfileResponse) {
        heroName = profileResponse.userdetails[0].uname
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


    private fun sendDogsitReqAPI() {
        dogsittingmodel.sendAllDogsitReq(
            MyApp.getSharedPref().userId,
            dogsitHeroId,
            valuePetId,
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

    /*private fun navigatePage() {
        startActivity(Intent(this, SendRequestActivity::class.java)
            .putExtra("receiveId", dogsitHeroId)
            .putExtra("petId", valuePetId)
            .putExtra("from","hero")
            .putExtra("heroname", heroName))
        finish()
    }*/

    private fun navigateTo(message: String?) {
        CommonMethod.showSnack(binding.root, message)
        startActivity(Intent(this, SuccessSendRequestActivity::class.java)
            .putExtra("from", "hero")
            .putExtra("heroname", heroName)
            .putExtra("heroId", dogsitHeroId))
        finish()
    }

}
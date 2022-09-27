package com.bnb.doggydoo.mydog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityDdpactivityBinding
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DDPActivity : AppCompatActivity() {

    private  var binding: ActivityDdpactivityBinding? = null
    private val bind get() = binding!!
    private lateinit var myDogViewModel: MyDogViewModel
    private var petId: String = ""
    private var userId: String = ""
    private var petImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDdpactivityBinding.inflate(layoutInflater)
        setContentView(bind.root)

        getInit()
        callGetDogDescriptionAPI()
        intents()
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        petId = intent.getStringExtra("petId").toString()


        /* val viewPager = binding.sliderviewpager
         adapter = ImageViewPagerAdapter(this)
         viewPager.adapter = adapter
         binding.dotsIndicator.setViewPager2(viewPager)*/

        bind.ivBack.setOnClickListener {
            finish()
        }
    }

    fun intents(){
        bind.resolved.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext,MarkResolved::class.java))
        })
    }


    private fun callGetDogDescriptionAPI(){
        myDogViewModel.getPetDescriptionData(petId, MyApp.getSharedPref().userId)
            .observe(this, androidx.lifecycle.Observer {
                when(it.status){
                    Result.Status.LOADING -> {
                        bind.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        bind.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                bind.parent
                            )
                            return@Observer
                        }
                        setDataInUI(it.data!!)
                    }
                    Result.Status.ERROR -> {
                        bind.progressBar.hide()
                        it.message?.snack(Color.RED, bind.parent)
                    }
            }})
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(data: PetDescriptionResponse) {
        petId = data.petdetail.id
        userId = data.userdetail.id
        bind.tvdiscription.text = data.petdetail.pet_description

        if (data.petImage.isNotEmpty()) {
            petImage = ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
            bind.dogimg.loadImageFromString(
                this,
                ApiConstant.PET_IMAGE_BASE_URL + data.petImage[0]
            )
            bind.dogimg.show()
        } else {
            Glide.with(this).load(R.drawable.dummy_pet).into(bind.dogimg)
        }
    }

    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
    }
}

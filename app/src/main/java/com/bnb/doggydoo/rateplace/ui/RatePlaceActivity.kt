package com.bnb.doggydoo.rateplace.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.inVisible
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityRatePlaceBinding
import com.bnb.doggydoo.rateplace.viewmodel.RateViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatePlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatePlaceBinding
    private val rateViewModel: RateViewModel by viewModels()
    private var rate = "0"
    private var isPetFriendly = "0"
    private var parkId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatePlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
    }

    private fun getInit() {
        parkId = intent.getStringExtra("partId").toString()
        binding.backButton.setOnClickListener { finish() }
        binding.ivExpressIcon1.show()
        binding.tvExpressType1.show()

        rate = "0"
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {

                when (i) {
                    in 2..19 -> {
                        rate = "1"
                        binding.ivExpressIcon1.show()
                        binding.tvExpressType1.show()

                        binding.ivExpressIcon2.inVisible()
                        binding.tvExpressType2.inVisible()

                        binding.ivExpressIcon3.inVisible()
                        binding.tvExpressType3.inVisible()

                        binding.ivExpressIcon4.inVisible()
                        binding.tvExpressType4.inVisible()

                        binding.ivExpressIcon5.inVisible()
                        binding.tvExpressType5.inVisible()

                    }
                    in 21..39 -> {
                        rate = "2"
                        binding.ivExpressIcon2.show()
                        binding.tvExpressType2.show()

                        binding.ivExpressIcon1.inVisible()
                        binding.tvExpressType1.inVisible()

                        binding.ivExpressIcon3.inVisible()
                        binding.tvExpressType3.inVisible()

                        binding.ivExpressIcon4.inVisible()
                        binding.tvExpressType4.inVisible()

                        binding.ivExpressIcon5.inVisible()
                        binding.tvExpressType5.inVisible()

                    }
                    in 41..59 -> {
                        rate = "3"
                        binding.ivExpressIcon3.show()
                        binding.tvExpressType3.show()

                        binding.ivExpressIcon2.inVisible()
                        binding.tvExpressType2.inVisible()

                        binding.ivExpressIcon1.inVisible()
                        binding.tvExpressType1.inVisible()

                        binding.ivExpressIcon4.inVisible()
                        binding.tvExpressType4.inVisible()

                        binding.ivExpressIcon5.inVisible()
                        binding.tvExpressType5.inVisible()


                    }
                    in 61..79 -> {
                        rate = "4"
                        binding.ivExpressIcon4.show()
                        binding.tvExpressType4.show()

                        binding.ivExpressIcon2.inVisible()
                        binding.tvExpressType2.inVisible()

                        binding.ivExpressIcon1.inVisible()
                        binding.tvExpressType1.inVisible()

                        binding.ivExpressIcon3.inVisible()
                        binding.tvExpressType3.inVisible()

                        binding.ivExpressIcon5.inVisible()
                        binding.tvExpressType5.inVisible()

                    }
                    in 81..100 -> {
                        rate = "5"
                        binding.ivExpressIcon5.show()
                        binding.tvExpressType5.show()

                        binding.ivExpressIcon2.inVisible()
                        binding.tvExpressType2.inVisible()

                        binding.ivExpressIcon1.inVisible()
                        binding.tvExpressType1.inVisible()

                        binding.ivExpressIcon3.inVisible()
                        binding.tvExpressType3.inVisible()

                        binding.ivExpressIcon4.inVisible()
                        binding.tvExpressType4.inVisible()
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        binding.postReview.setOnClickListener {
            if (binding.petFriendly.isChecked) {
                isPetFriendly = "1"
            } else {
                isPetFriendly = "0"
            }
            if (rate != "0"){
                callPostRateAPI()
            }else{
                Toast.makeText(this, "Please rate the place.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun callPostRateAPI() {
        rateViewModel.getRateData(
            MyApp.getSharedPref().userId, parkId, rate, binding.reviewText.text.toString().trim(),
            isPetFriendly
        ).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                    binding.postReview.isEnabled = false
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.responseCode.equals("0")){
                        binding.postReview.isEnabled=true
                        it.data?.responseMessage?.snack(Color.RED, binding.parent)
                        return@Observer
                    }
                    setResult(RESULT_OK)
                    finish()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    binding.postReview.isEnabled = true
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }
}
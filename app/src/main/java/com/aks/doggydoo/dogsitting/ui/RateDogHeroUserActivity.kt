package com.aks.doggydoo.dogsitting.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.badge.BadgeActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.inVisible
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityRateUserBinding
import com.aks.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDogHeroUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRateUserBinding
    private lateinit var dogsittingmodel: DogsittingModel
    private var heroId: String =""
    private var heroName: String =""
    private var rateCount:String ="1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)

        heroId = intent.getStringExtra("heroId").toString()
        heroName = intent.getStringExtra("heroName").toString()

        binding.tvTitleName.text = heroName

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvReportDelivery.setOnClickListener{
            rateHeroUserAPI()
        }

        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {

                if (i > 1 && i < 20){
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
                    rateCount="1"

                }else if (i > 20 &&  i < 40) {
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
                    rateCount="2"

                } else if (i > 40 &&  i < 60) {
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
                    rateCount="3"

                }else if (i > 60 &&  i < 80) {
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
                    rateCount="4"

                }else if (i > 80 &&  i <= 100) {
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
                    rateCount="5"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }


    private fun rateHeroUserAPI() {
        dogsittingmodel.rateHeroUser(
            MyApp.getSharedPref().userId,
           heroId,
            rateCount,
            binding.etFeedback.text.trim().toString()
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        Toast.makeText(this,it.data?.responseMessage, Toast.LENGTH_SHORT).show()
                        if (it.data!!.responseCode == ("0")) {
                            it.data.responseMessage.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }

                        navigateTo()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun navigateTo() {
        startActivity(Intent(this, BadgeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}
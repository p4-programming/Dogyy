package com.bnb.doggydoo.dogsitting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.ActivitySuccessRequestSentBinding
import com.bnb.doggydoo.dogsitrequestmsg.DogsitRequestMessageActivity
import com.bnb.doggydoo.utils.CommonMethod

class SuccessSendRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessRequestSentBinding
    private var from:String =""
    private var heroName: String =""
    private var heroId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessRequestSentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
    }

    private fun getInit() {
        from = intent.getStringExtra("from").toString()
        heroName = intent.getStringExtra("heroname").toString()
        heroId =  intent.getStringExtra("heroId").toString()

        if (from=="hero"){
            binding.tvMessage.text = "Rate"
            binding.tvMessage.setOnClickListener {
                startActivity(Intent(this, RateDogHeroUserActivity::class.java)
                    .putExtra("heroId",heroId)
                    .putExtra("heroName", heroName))
                finish()
            }
        }else{
            binding.tvMessage.show()
            binding.tvMessage.text ="Message"+" "+ heroName
            binding.tvMessage.setOnClickListener {
                startActivity(Intent(this, DogsitRequestMessageActivity::class.java))
                finish()
            }
        }

        binding.tvGoBack.setOnClickListener {
            finish()
        }

    }
}
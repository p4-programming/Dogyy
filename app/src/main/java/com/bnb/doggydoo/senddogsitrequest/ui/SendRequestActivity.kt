package com.bnb.doggydoo.senddogsitrequest.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivitySendRequestBinding
import com.bnb.doggydoo.dogsitting.ui.SuccessSendRequestActivity
import com.bnb.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SendRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendRequestBinding
    private val dogsittingmodel: DogsittingModel by viewModels()
    private val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private var petID: String = ""
    private var receivedID: String = ""
    private var selectedTime: String = ""
    private var selectedDate:String =""
    private var from:String =""
    private var heroName: String =""
    var selectDateFlag:String ="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        petID = intent.getStringExtra("petId").toString()
        receivedID = intent.getStringExtra("receiveId").toString()
        from = intent.getStringExtra("from").toString()
        heroName = intent.getStringExtra("heroname").toString()


        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.cvSendReq.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            selectDateFlag ="1"
            val calender: Calendar = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            calView.setDate(calender.timeInMillis, true, true)
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            binding.tvSelectedDate.text = selectedDate
        }

        binding.fromNumber.maxValue = 24
        binding.fromNumber.minValue = 0
        binding.toNumber.maxValue = 59
        binding.toNumber.minValue = 0

        binding.tvReqDogsit.setOnClickListener {
            if (selectDateFlag=="1"){
                sendDogsitReqAPI()
            }else{
                Toast.makeText(this,"Please select a date", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun sendDogsitReqAPI() {
        selectedTime = binding.fromNumber.value.toString() + ":" + binding.toNumber.value.toString()
        if (selectedDate == "")
            selectedDate = currentDate

        dogsittingmodel.sendAllDogsitReq(
            MyApp.getSharedPref().userId,
            receivedID,
            petID,
            selectedDate,
            selectedTime
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

    private fun navigateTo(message: String?) {
        CommonMethod.showSnack(binding.root, message)
        startActivity(Intent(this@SendRequestActivity, SuccessSendRequestActivity::class.java)
            .putExtra("from", from)
            .putExtra("heroname", heroName)
            .putExtra("heroId", receivedID))
        finish()
    }
}
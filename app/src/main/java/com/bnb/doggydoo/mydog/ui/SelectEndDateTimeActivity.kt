package com.bnb.doggydoo.mydog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivitySelectdatetimeBinding
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SelectEndDateTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectdatetimeBinding
    private val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private var selected2Time: String = ""
    private var selected2Date: String = ""
    private var selected1Time: String = ""
    private var selected1Date: String = ""
    private var petId: String = ""
    private lateinit var myDogViewModel: MyDogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectdatetimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    @SuppressLint("SetTextI18n")
    private fun getInit() {
        binding.tvSelectDateTime.text = "Select End Time"
        selected1Date = intent.getStringExtra("date1").toString()
        selected1Time = intent.getStringExtra("time1").toString()
        petId = intent.getStringExtra("petId").toString()
        binding.llStart.show()
        binding.tvStart.text = "$selected1Date ,$selected1Time"
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.cvSendReq.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val calender: Calendar = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            calView.setDate(calender.timeInMillis, true, true)
            selected2Date = "$year-${month + 1}-$dayOfMonth"
        }

        binding.fromNumber.maxValue = 24
        binding.fromNumber.minValue = 0
        binding.toNumber.maxValue = 59
        binding.toNumber.minValue = 0

        if (selected2Date == "") {
            selected2Date = currentDate
        }

        binding.tvConfirmDateTime.setOnClickListener {
            selected2Time = binding.fromNumber.value.toString() + ":" + binding.toNumber.value.toString()

            if (selected2Date.isNotEmpty() && selected2Time.isNotEmpty()){
                updatePetInfoAPI()
            }else{
                Toast.makeText(this, "Select end date and time.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updatePetInfoAPI() {
        myDogViewModel.getPetStatusUpdateData(
            petId,
            "1",
            "doggysit",
            selected1Date,
            selected1Time,
            selected2Date,
            selected2Time
        ).observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.responseCode.equals("0")) {
                        it.data?.responseMessage?.snack(
                            Color.RED,
                            binding.parent
                        )
                        return@Observer
                    }
                    showMessage()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }

    private fun showMessage() {
        CommonMethod.showSnack(binding.root, "Status changed successfully!!")
        startActivity(
            Intent(this, MyDog::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}
package com.bnb.doggydoo.mydog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.databinding.ActivitySelectdatetimeBinding
import java.text.SimpleDateFormat
import java.util.*

class SelectStartDateTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectdatetimeBinding
    private val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private var selectedTime: String = ""
    private var selectedDate: String = ""
    private var petId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectdatetimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    @SuppressLint("SetTextI18n")
    private fun getInit() {
        binding.tvSelectDateTime.text= "Select Start Time"
        petId = intent.getStringExtra("petId").toString()
        binding.llStart.hide()

        binding.cvSendReq.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val calender: Calendar = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            calView.setDate(calender.timeInMillis, true, true)
            selectedDate = "$year-${month + 1}-$dayOfMonth"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.fromNumber.maxValue = 24
        binding.fromNumber.minValue = 0
        binding.toNumber.maxValue = 59
        binding.toNumber.minValue = 0

        if (selectedDate == "") {
            selectedDate = currentDate
        }

        binding.tvConfirmDateTime.setOnClickListener {
            selectedTime = binding.fromNumber.value.toString() + ":" + binding.toNumber.value.toString()

            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()){
                startActivity(
                    Intent(this@SelectStartDateTimeActivity, SelectEndDateTimeActivity::class.java)
                        .putExtra("date1", selectedDate)
                        .putExtra("time1", selectedTime)
                        .putExtra("petId",petId)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }else{
                Toast.makeText(this, "Select start date and time.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
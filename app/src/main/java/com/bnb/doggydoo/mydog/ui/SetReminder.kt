package com.bnb.doggydoo.mydog.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.commonutility.*
import com.bnb.doggydoo.databinding.ActivitySetReminderBinding
import com.bnb.doggydoo.mydog.adapter.SelectReminderAdapter
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*



@AndroidEntryPoint
class SetReminder : AppCompatActivity() {
    private lateinit var binding: ActivitySetReminderBinding
    private lateinit var myDogViewModel: MyDogViewModel
    private val keysForReminderType =
        listOf("Bathing", "Grooming", "Vet Appointment", "Vaccination", "Walk", "Feed", "Other")
    private var petReminderTypeId: String = ""
    private var dateFormat: String = ""
    private var timeInHr: String = ""
    private var timeInMin: String = ""
    private val currentHr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private val currentMin = Calendar.getInstance().get(Calendar.MINUTE)
    private val currentDate = SimpleDateFormat("yyyy/M/dd").format(Date())
    var petId =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()

    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        binding.reminderRv.adapter = SelectReminderAdapter(this, {
            if (it)
                binding.setOtherText.show()
            else binding.setOtherText.hide()
        }) {
            petReminderTypeId = keysForReminderType[it]
        }
        binding.backButton.setOnClickListener { onBackPressed() }

        /**
         * GET Selected date from CalenderView
         */
        binding.calender.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val newMonth = month + 1
            if (newMonth > 9) {
                if (dayOfMonth > 9)
                    dateFormat = "$year-$newMonth-$dayOfMonth"
                else
                    dateFormat = "$year-$newMonth-0$dayOfMonth"
            } else {
                dateFormat = "$year-0$newMonth-$dayOfMonth"
            }
        }

        binding.selectReminder.setOnClickListener {
            if (binding.selectReminder.text == "Select Reminder Type") {
                binding.setOtherText.hide()
                binding.reminderRv.hide()
                binding.reminderText.hide()
                binding.setCalendarLayout.show()
                binding.selectReminder.text = "Set Reminder"
            } else {
                callPetReminderPostAPI()
            }
        }
        binding.hrNumber.maxValue = 12
        binding.hrNumber.minValue = 1
        binding.hrNumber.value = currentHr
        binding.minuteNumber.maxValue = 60
        binding.minuteNumber.minValue = 1
        binding.minuteNumber.value = currentMin

        binding.hrNumber.setOnValueChangedListener { picker, oldVal, newVal ->
            timeInHr = "$newVal"
        }
        binding.minuteNumber.setOnValueChangedListener { picker, oldVal, newVal ->
            timeInMin = "$newVal"
        }
        binding.type.setOnClickListener {
            if (binding.type.text.toString() == "AM") {
                binding.type.text = "PM"
            } else {
                binding.type.text = "AM"
            }
        }
    }

    private fun callPetReminderPostAPI() {
        petId = intent.getStringExtra("petId").toString();
        if (timeInHr == "") {
            timeInHr = "$currentHr"
        }
        if (timeInMin == "") {
            timeInMin = "$currentMin"
        }
        if (dateFormat == "") {
            dateFormat = "$currentDate"
        }
        val format12 = "$timeInHr:$timeInMin ${binding.type.text}"
        val format24 = format12.convertTimeIn24HrFormat()
        if (petReminderTypeId == "Other"){
            petReminderTypeId = "other"
        }

        //Toast.makeText(this, petReminderTypeId, Toast.LENGTH_SHORT).show()
        println("data is>>"+ petId)
        println("data is>>"+ petReminderTypeId)
        println("data is>>"+ "$dateFormat $format24:00")
        println("data is>>"+ binding.setOtherText.text.toString())


        myDogViewModel.getPostReminderData(
            petId,
            petReminderTypeId,
            "$dateFormat $format24:00",
            binding.setOtherText.text.toString(),
            MyApp.getSharedPref().userId
        ).observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                    binding.selectReminder.isEnabled = false
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.selectReminder.isEnabled = true
                    if (it.data?.responseCode.equals("0")) {
                        it.data?.responseMessage?.snack(
                            Color.RED,
                            binding.parent
                        )
                        return@Observer
                    }
                    setResult(RESULT_OK)
                    Toast.makeText(this,"Reminder uploaded successfully!!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                Result.Status.ERROR -> {
                    binding.selectReminder.isEnabled = true
                    binding.progressBar.hide()
                    "Unable to add reminder".snack(Color.RED, binding.parent)
                }
            }
        })
    }
}
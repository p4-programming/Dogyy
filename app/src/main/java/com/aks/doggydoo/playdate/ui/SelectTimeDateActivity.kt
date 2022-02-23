package com.aks.doggydoo.playdate.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityPlaydateTimedateBinding
import com.aks.doggydoo.newsfeed.util.RecyclerTouchListener
import com.aks.doggydoo.playdate.adapter.SelectPetAdapter
import com.aks.doggydoo.playdate.viewmodel.PlayDateViewModel
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class SelectTimeDateActivity : AppCompatActivity() {
    private var dateMode: String = "1"
    private lateinit var binding: ActivityPlaydateTimedateBinding
    private val currentDate = SimpleDateFormat("yyyy/M/dd").format(Date())
    private var dateFormat: String = ""
    private val playDateViewModel: PlayDateViewModel by viewModels()
    private lateinit var adapter: SelectPetAdapter
    private var petID: String = ""
    private var receivedID: String = ""
    private var selectedTime: String = ""
    private var breed = ArrayList<String>()
    private var breedIdList = ArrayList<String>()
    private var valueBreed: String = ""
    private var valueBreedId: String = ""
    private var fromValue:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaydateTimedateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        allPetRequest()
    }

    private fun getInit() {
        petID = intent.getStringExtra("receiveId").toString()
        receivedID = intent.getStringExtra("petId").toString()
        fromValue = intent.getStringExtra("from").toString()

        if (fromValue.isNotEmpty()){
            if (fromValue == "other"){
                binding.petLayout.show()
                valueBreedId = petID
            }
        }else{
            binding.petLayout.show()
        }

        binding.calender.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            val calender: Calendar = Calendar.getInstance()
            calender.set(year, month, dayOfMonth)
            calView.setDate(calender.timeInMillis, true, true)
            dateFormat = "$year-${month + 1}-$dayOfMonth"
        }

        binding.fromNumber.maxValue = 24
        binding.fromNumber.minValue = 0
        binding.toNumber.maxValue = 59
        binding.toNumber.minValue = 0

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.buttonRequest.setOnClickListener {
            if (fromValue.isNotEmpty()){
                if (fromValue == "other"){
                    //callSendPlayDateRequest

                    if (binding.tvPetBreed.text == "Select Pet") {
                        Toast.makeText(applicationContext, "Please select pet.", Toast.LENGTH_SHORT).show()
                    } else {
                        callSendPlayDateRequest()
                    }
                }

            }/*else{
                if (binding.tvPetBreed.text == "Select Pet") {
                    Toast.makeText(applicationContext, "Please select pet.", Toast.LENGTH_SHORT).show()
                } else {
                    callSendPlayDateRequest()
                }
            }*/


        }

        binding.llPet.hide()
        binding.tvPetBreed.show()
        binding.petLayout.setOnClickListener {
            binding.llPet.show()
            binding.tvPetBreed.hide()
        }

        binding.tvConfirm.setOnClickListener {
            binding.llPet.hide()
            binding.tvPetBreed.show()
        }

        adapter = SelectPetAdapter(this)
        binding.rvPet.adapter = adapter
        binding.rvPet.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.rvPet,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        valueBreed = breed.get(position)
                        valueBreedId = breedIdList.get(position)
                        binding.tvPetBreed.text = valueBreed
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }


    private fun allPetRequest() {
        playDateViewModel.getAllMyPet(
            MyApp.getSharedPref().userId,
        ).observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    for (category in it.data.mypetdetail) {
                        breed.add(category.pet_name)
                        breedIdList.add(category.id)
                    }

                    adapter.submitList(it.data.mypetdetail)
                }
                Result.Status.ERROR -> {
                    it.message!!.snack(Color.RED, binding.root)
                    binding.progressBar.hide()
                }
            }
        })
    }


    private fun callSendPlayDateRequest() {
        if (binding.fromNumber.value.toString() == "24") {
            selectedTime = binding.fromNumber.value.toString() + ":" + "00"
        } else {
            selectedTime =
                binding.fromNumber.value.toString() + ":" + binding.toNumber.value.toString()
        }


        System.out.println("req data>>" + MyApp.getSharedPref().userId)
        System.out.println("req data>>" + receivedID)
        System.out.println("req data>>" + valueBreedId)
        System.out.println("req data>>" + dateFormat)
        System.out.println("req data>>" + selectedTime)
        System.out.println("req data>>" + dateMode)

        if (dateFormat == "")
            dateFormat = "$currentDate"
        playDateViewModel.sentRequestData(
            MyApp.getSharedPref().userId,
            petID,
            receivedID,
            dateFormat,
            selectedTime,
            "1",
            valueBreedId
        ).observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    navigateTo()

                }
                Result.Status.ERROR -> {
                    it.message!!.snack(Color.RED, binding.root)
                    binding.progressBar.hide()
                }
            }
        })
    }

    private fun navigateTo() {
        CommonMethod.showSnack(binding.root, "Playdate request sent successfully!!")
        startActivity(Intent(this, PlayDateSuccessSendRequestActivity::class.java))
        finish()
    }
}
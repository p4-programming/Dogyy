package com.aks.doggydoo.callawet.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.callawet.adapter.CallingStateAdapter
import com.aks.doggydoo.callawet.viewmodel.CallReasonModel
import com.aks.doggydoo.callawet.viewmodel.MakeCallModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityReasonForCallingDocBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReasonForCallingDoc : AppCompatActivity() {
    private lateinit var binding: ActivityReasonForCallingDocBinding
    var fromLocation = ""
    private lateinit var adapter: CallingStateAdapter
    private lateinit var callReasonModel: CallReasonModel
    private lateinit var makeCallModel: MakeCallModel
    var callType =""
    var selectedValue = ""
    private var phoneNo:String =""
    private lateinit var telephonyManager: TelephonyManager

    private val requestArray = arrayOf(
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReasonForCallingDocBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        fetchCallReason()
    }

    private fun getInit() {
        callReasonModel = ViewModelProvider(this).get(CallReasonModel::class.java)
        makeCallModel = ViewModelProvider(this).get(MakeCallModel::class.java)

        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )

        adapter = CallingStateAdapter(this)
        binding.stateReasonRv.adapter = adapter
        binding.backButton.setOnClickListener { finish() }

        fromLocation = intent.getStringExtra("origin").toString()
        phoneNo = intent.getStringExtra("phone_no").toString()
        System.out.println("selected item>>" + fromLocation)


        if (fromLocation.equals("nearbyDoc")) {
            binding.tvcallVet.text = getString(R.string.call_doc)
            callType = "1"
        } else {
            binding.tvcallVet.text = getString(R.string.call_clinic)
            callType ="2"
        }

        binding.tvcallVet.setOnClickListener {
            if (selectedValue.isEmpty()){
                Toast.makeText(this, "Kindly select a reason for calling.", Toast.LENGTH_SHORT).show()
            }else{
                Call()
            }

        }

        adapter.onItemClick = { it ->
            selectedValue = it.toString()
        }
    }


    private fun fetchCallReason() {
        callReasonModel.getCallReason().observe(this, Observer {
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
                    adapter.submitList(it.data.reasonDetailList)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }

    private fun makeCall() {
        makeCallModel.getCallData(
            MyApp.getSharedPref().userId,
            MyApp.getSharedPref().callerId,
            selectedValue,
            callType
        ).observe(this, Observer {
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
                    MyApp.getSharedPref().callId = it.data.callid
                    //callToNumber()
                    if (fromLocation.equals("nearbyDoc")) {
                        startActivity(
                            Intent(this, RateDoctorActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    } else {
                        finish()
                    }
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }


    private fun Call() {
        val callNumber = "tel:$phoneNo"
        if (callNumber.isNotEmpty()){
            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse(callNumber)
            try {
                startActivity(phoneIntent)
                finish()
                //Finished making a call
                makeCall()
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Call failed, please try again later.", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No number found.", Toast.LENGTH_SHORT).show()
        }

    }
}
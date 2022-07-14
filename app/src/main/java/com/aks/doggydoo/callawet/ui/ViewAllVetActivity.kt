package com.aks.doggydoo.callawet.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.R
import com.aks.doggydoo.callawet.adapter.NearbyDoctorAdapter
import com.aks.doggydoo.callawet.adapter.NearbyHospitalAdapter
import com.aks.doggydoo.callawet.viewmodel.CallaWetModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityViewAllVetBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllVetActivity : AppCompatActivity() {
    private var _binding: ActivityViewAllVetBinding? = null
    private val binding get() = _binding!!
    private lateinit var callawetModel: CallaWetModel
    private lateinit var adapternerabydoc: NearbyDoctorAdapter
    private lateinit var adapternerabyhos: NearbyHospitalAdapter
    private var viewType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewAllVetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        callawetModel = ViewModelProvider(this).get(CallaWetModel::class.java)

        viewType = intent.getStringExtra("type").toString()
        binding.rvAllItems.layoutManager = GridLayoutManager(this@ViewAllVetActivity, 2)

        adapternerabydoc = NearbyDoctorAdapter(this@ViewAllVetActivity)
        adapternerabyhos = NearbyHospitalAdapter(this@ViewAllVetActivity)
        binding.rvAllItems.adapter = adapternerabydoc
        binding.rvAllItems.adapter = adapternerabyhos

        if (viewType == "alldoc") {
            binding.tvTitle.text = "All Doctors"
            callAllDocAPI()
            adapternerabydoc = NearbyDoctorAdapter(this@ViewAllVetActivity)
            binding.rvAllItems.adapter = adapternerabydoc
        } else {
            binding.tvTitle.text = "All Hospitals"
            callAllClinicAPI()
            adapternerabyhos = NearbyHospitalAdapter(this@ViewAllVetActivity)
            binding.rvAllItems.adapter = adapternerabyhos
        }

        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.rvAllItems.clearAnimation()
            if (viewType == "alldoc") {
                binding.tvTitle.text = "All Doctors"
                callAllDocAPI()
                adapternerabydoc = NearbyDoctorAdapter(this@ViewAllVetActivity)
                binding.rvAllItems.adapter = adapternerabydoc
            } else {
                binding.tvTitle.text = "All Hospitals"
                callAllClinicAPI()
                adapternerabyhos = NearbyHospitalAdapter(this@ViewAllVetActivity)
                binding.rvAllItems.adapter = adapternerabyhos
            }
            binding.itemsswipetorefresh.isRefreshing = false
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }


    private fun callAllClinicAPI() {
        callawetModel.getAllClinicList(MyApp.getSharedPref().userId)
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
                        adapternerabyhos.submitList(it.data.nearByHosList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun callAllDocAPI() {
        callawetModel.getAllDocList(MyApp.getSharedPref().userId)
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
                        adapternerabydoc.submitList(it.data.nearByDoclist)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}
package com.aks.doggydoo.callawet.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.adoptdogdetails.adapter.AvailableDocAdapter
import com.aks.doggydoo.callawet.datasource.model.nearbydoc.NearByDoctorDetailList
import com.aks.doggydoo.callawet.datasource.model.nearbyhos.ClinicDetailDetailList
import com.aks.doggydoo.callawet.viewmodel.NearByDoctorDetailModel
import com.aks.doggydoo.callawet.viewmodel.NearByHospitalDetailModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityDocDetailsBinding
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocDetailsBinding
    var clickedId =""
    var fromLocation =""
    var image_url =""
    private lateinit var adapter: AvailableDocAdapter
    private lateinit var doctorDetailModel: NearByDoctorDetailModel
    private lateinit var nearByHospitalDetailModel: NearByHospitalDetailModel
    private var phoneNumber: String =""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocDetailsBinding.inflate(layoutInflater)
        getInit()

        binding.backButton.setOnClickListener {
            supportFinishAfterTransition()
        }

        binding.rlCallClinic.setOnClickListener {
            startActivity(Intent(this, ReasonForCallingDoc::class.java)
                .putExtra("origin", fromLocation)
                .putExtra("phone_no", phoneNumber))
        }
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun getInit() {
        doctorDetailModel = ViewModelProvider(this).get(NearByDoctorDetailModel::class.java)
        nearByHospitalDetailModel = ViewModelProvider(this).get(NearByHospitalDetailModel::class.java)

        clickedId = intent.getStringExtra("id").toString()

        //Toast.makeText(this, clickedId, Toast.LENGTH_SHORT).show()

        fromLocation = intent.getStringExtra("from").toString()

        if (fromLocation == "nearbyDoc"){
            binding.docAvailableRv.hide()
            binding.llClinic.visibility = View.VISIBLE
            binding.tvAvailableTitle.text ="Doctor's Clinic"
            fetchDoctorDetail()
        }else{
            binding.docAvailableRv.show()
            binding.llClinic.visibility = View.GONE
            binding.tvAvailableTitle.text ="Doctors Avialable"
            adapter = AvailableDocAdapter(this)
            binding.docAvailableRv.adapter = adapter
            fetchHospitalDetail()
        }
    }

    private fun fetchDoctorDetail() {
        doctorDetailModel.fetDoctorDetailList(clickedId,MyApp.getSharedPref().userId).observe(this, Observer {
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
                    MyApp.getSharedPref().callerId = it.data.doctorDetailList.get(0).id
                    setDataInUI(it.data.doctorDetailList)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }

    private fun setDataInUI(doctorDetailList: List<NearByDoctorDetailList>) {
        phoneNumber = doctorDetailList[0].phone
        binding.ivDoc.loadImageFromString(
            this@DoctorDetailActivity,
            ApiConstant.NEARBY_DOC_IMAGE_BASE_URL + doctorDetailList[0].Vet_image
        )
        binding.tvDocName.text = doctorDetailList[0].name
        binding.tvDistance.text = doctorDetailList[0].km +" Km"
        binding.tvDescription.text = doctorDetailList[0].Description
        binding.ivClinic.loadImageFromString(
            this@DoctorDetailActivity,
            ApiConstant.NEARBY_HOS_IMAGE_BASE_URL + doctorDetailList[0].Clinic_image
        )
        binding.tvClinicName.text = doctorDetailList[0].Clinic_name

    }

    private fun fetchHospitalDetail() {
        nearByHospitalDetailModel.fetHospitalDetailList(clickedId,MyApp.getSharedPref().userId).observe(this, Observer {
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
                    MyApp.getSharedPref().callerId = it.data.clinicDetailList.get(0).id
                    adapter.submitList(it.data.doctorlist)
                    setDataInUIHos(it.data.clinicDetailList)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }

    private fun setDataInUIHos(clinicDetailList: List<ClinicDetailDetailList>) {
        binding.ivDoc.loadImageFromString(
            this@DoctorDetailActivity,
            ApiConstant.NEARBY_HOS_IMAGE_BASE_URL + clinicDetailList[0].place_image[0]
        )
        binding.tvDocName.text = clinicDetailList[0].name
        binding.tvDistance.text = clinicDetailList[0].km +" Km"
        binding.tvDescription.text = clinicDetailList[0].place_description
    }


}
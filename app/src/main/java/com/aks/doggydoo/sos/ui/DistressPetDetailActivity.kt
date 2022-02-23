package com.aks.doggydoo.sos.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityDistressPetDetailBinding
import com.aks.doggydoo.homemodule.ui.HomeActivity
import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.Distresspetdetail
import com.aks.doggydoo.mydog.viewmodel.MyDogViewModel
import com.aks.doggydoo.utils.MultipartFile
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.aks.doggydoo.utils.network.ApiConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DistressPetDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDistressPetDetailBinding
    private var petId: String = ""
    private val myDogViewModel: MyDogViewModel by viewModels()
    private var latt: String = ""
    private var longg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistressPetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        petId = intent.getStringExtra("id").toString()
        if (!petId.isEmpty()) {
            getPetAPI()
        } else {
            Toast.makeText(this, "Oops!! Please try after sometime.", Toast.LENGTH_SHORT).show()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvNavigate.setOnClickListener {
            if (!latt.isEmpty() && !longg.isEmpty()){
                val builder: Uri.Builder = Uri.Builder()
                builder.scheme("https")
                    .authority("www.google.com")
                    .appendPath("maps")
                    .appendPath("dir")
                    .appendPath("")
                    .appendQueryParameter("api", "1")
                    .appendQueryParameter(
                        "destination",
                        latt + "," + longg
                    )
                val url: String = builder.build().toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

        }
    }

    private fun getPetAPI() {
        myDogViewModel.getDistressPetDetailData(petId)
            .observe(this, Observer {
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
                        setUI(it.data!!.distressPetdetail)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun setUI(distressPetdetail: Distresspetdetail) {
        binding.tvDescription.text = distressPetdetail.pet_description
        latt = distressPetdetail.lattitute
        longg = distressPetdetail.longitute
        binding.ivDog.loadImageFromString(
            this,
            ApiConstant.PET_IMAGE_BASE_URL + distressPetdetail.petImage[0]
        )
    }
}
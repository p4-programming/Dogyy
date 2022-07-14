package com.aks.doggydoo.adoption.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.adoption.adapter.ViewAllShelterAdapter
import com.aks.doggydoo.adoption.viewmodel.AdoptionModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityShelterViewAllBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptionViewAllShelter : AppCompatActivity() {
    private lateinit var binding: ActivityShelterViewAllBinding
    private lateinit var adapter: ViewAllShelterAdapter
    private lateinit var adoptionModel: AdoptionModel
    var fromValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShelterViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        callGetShelterListApi()
    }

    private fun getInit() {
        adoptionModel = ViewModelProvider(this).get(AdoptionModel::class.java)

        fromValue = intent.getStringExtra("from").toString()
        adapter = ViewAllShelterAdapter(this@AdoptionViewAllShelter)
        binding.rvAllItems.layoutManager = GridLayoutManager(this@AdoptionViewAllShelter, 3)
        binding.rvAllItems.adapter = adapter

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun callGetShelterListApi() {
        adoptionModel.getAllShelterList(MyApp.getSharedPref().userId).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode == ("0")) {
                        it.data.responseMessage.snack(
                            Color.RED,
                            binding.root
                        )
                        return@Observer
                    }
                    adapter.submitList(it.data.shelterlist)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }
}
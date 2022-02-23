package com.aks.doggydoo.playdate.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityViewallBinding
import com.aks.doggydoo.playdate.adapter.ViewAllPlaydateAdapter
import com.aks.doggydoo.playdate.datasource.model.homepage.LastPlayDates
import com.aks.doggydoo.playdate.viewmodel.PlayDateViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllPlayDateActivity : AppCompatActivity() {
    private var _binding: ActivityViewallBinding? = null
    private val binding get() = _binding!!
    private val playDateViewModel: PlayDateViewModel by viewModels()
    private lateinit var viewAllAdapter: ViewAllPlaydateAdapter
    private var viewType: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callPetApi()
    }

    private fun getInit() {
        viewType = intent.getStringExtra("type").toString()
        viewAllAdapter = ViewAllPlaydateAdapter(this)
        binding.rvAll.layoutManager = GridLayoutManager(this, 2)
        binding.rvAll.adapter = viewAllAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewAllAdapter.filter.filter(s)
                viewAllAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun callPetApi() {
        playDateViewModel.getAllPet(MyApp.getSharedPref().userId,viewType,MyApp.getSharedPref().userLat,MyApp.getSharedPref().userLong)
            .observe(this, Observer {
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
                        renderList(it.data.nearbypets)
                       // viewAllAdapter.submitList(it.data.nearbypets)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun renderList(nearbypets: List<LastPlayDates>) {
        viewAllAdapter.addData(nearbypets)
    }
}
package com.aks.doggydoo.homemodule.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityViewAllReminderBinding
import com.aks.doggydoo.homemodule.adapter.ViewAllReminderAdapter
import com.aks.doggydoo.homemodule.datasource.model.home.UserReminderList
import com.aks.doggydoo.homemodule.viewmodel.HomeViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllReminderActivity : AppCompatActivity() {
    private var _binding: ActivityViewAllReminderBinding? = null
    private val binding get() = _binding!!
    private var viewType: String =""
    private lateinit var viewAllAdapter: ViewAllReminderAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewAllReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getAllReminder()
    }

    private fun getInit() {
        viewType = intent.getStringExtra("type").toString()

        viewAllAdapter = ViewAllReminderAdapter(this)
        binding.rvAll.layoutManager = GridLayoutManager(this, 2)
        binding.rvAll.adapter = viewAllAdapter

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getAllReminder() {
        System.out.println("user id>>" + MyApp.getSharedPref().userId)
        homeViewModel.getAllUserReminder(MyApp.getSharedPref().userId, viewType)
            .observe(this,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            binding.progressBar.hide()
                            if (it.data!!.responseCode == "0") {
                                it.message!!.snack(Color.RED, binding.root)
                                return@Observer
                            }
                            renderList(it.data.userReminderList)
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    private fun renderList(userReminderList: List<UserReminderList>) {
        viewAllAdapter.addData(userReminderList)
    }
}
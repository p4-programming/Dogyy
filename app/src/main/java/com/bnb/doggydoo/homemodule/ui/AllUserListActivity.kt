package com.bnb.doggydoo.homemodule.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityAllUserBinding
import com.bnb.doggydoo.homemodule.adapter.AllUserAdapter
import com.bnb.doggydoo.homemodule.datasource.model.home.AllUserList
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllUserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllUserBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var alluserAdapter: AllUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getAllFriendApi()
    }

    private fun getInit() {
        alluserAdapter = AllUserAdapter(this@AllUserListActivity)
        binding.rvAllUser.adapter = alluserAdapter
       // binding.rvAllUser.visibility = View.GONE

        binding.searchview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                alluserAdapter.filter.filter(s)
                alluserAdapter.notifyDataSetChanged()
               // binding.rvAllUser.visibility = View.VISIBLE
            }
        })

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getAllFriendApi() {
        System.out.println("user id>>" + MyApp.getSharedPref().userId)
        homeViewModel.getAllUser(MyApp.getSharedPref().userId)
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
                            renderList(it.data.allUserList)
                        }
                        Result.Status.ERROR -> {
                            binding.progressBar.hide()
                            it.message!!.snack(Color.RED, binding.root)
                        }
                    }
                })
    }

    private fun renderList(allUserList: List<AllUserList>) {
        alluserAdapter.addData(allUserList)
    }
}
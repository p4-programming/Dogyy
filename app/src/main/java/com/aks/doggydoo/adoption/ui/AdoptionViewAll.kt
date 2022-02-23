package com.aks.doggydoo.adoption.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.R
import com.aks.doggydoo.adoption.adapter.ViewAllAdapter
import com.aks.doggydoo.adoption.datasource.model.AdoptionListAllData
import com.aks.doggydoo.adoption.viewmodel.AdoptionModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityAdoptionViewAllBinding
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AdoptionViewAll : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionViewAllBinding
    private lateinit var adapter: ViewAllAdapter
    private lateinit var adoptionModel: AdoptionModel
    var fromValue = ""
    private var beans: List<AdoptionListAllData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callGetAdoptionListApi()
    }

    private fun getInit() {
        adoptionModel = ViewModelProvider(this).get(AdoptionModel::class.java)
        fromValue = intent.getStringExtra("from").toString()
        adapter = ViewAllAdapter(this@AdoptionViewAll)

        binding.rvAllItems.layoutManager = GridLayoutManager(this@AdoptionViewAll, 2)
        binding.rvAllItems.adapter = adapter

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                adapter.notifyDataSetChanged()
                System.out.println("search text >>" + s.toString())
            }
        })

        //** Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.cal))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        binding.itemsswipetorefresh.setOnRefreshListener {
            binding.rvAllItems.clearAnimation()
            callGetAdoptionListApi()
            adapter = ViewAllAdapter(this@AdoptionViewAll)
            binding.rvAllItems.adapter = adapter
            binding.itemsswipetorefresh.isRefreshing = false
        }
    }

    private fun callGetAdoptionListApi() {
        adoptionModel.getAllAdoptionList(MyApp.getSharedPref().userId).observe(this, Observer {
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
                    renderList(it.data.adoptionlist)

                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }

    private fun renderList(data: List<AdoptionListAllData>) {
        adapter.addData(data)
    }
}
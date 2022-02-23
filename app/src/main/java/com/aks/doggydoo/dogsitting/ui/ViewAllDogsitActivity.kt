package com.aks.doggydoo.dogsitting.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityViewallBinding
import com.aks.doggydoo.dogsitting.adapter.ViewAllDogsitAdapter
import com.aks.doggydoo.dogsitting.datasource.model.ViewAllDogsitPetList
import com.aks.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllDogsitActivity : AppCompatActivity() {
    private var _binding: ActivityViewallBinding? = null
    private val binding get() = _binding!!
    private var viewType: String =""
    private lateinit var dogsittingmodel: DogsittingModel
    private lateinit var viewAllAdapter: ViewAllDogsitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callDogsittingAPI()
    }

    private fun getInit() {
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)

        viewType = intent.getStringExtra("viewType").toString()
        viewAllAdapter = ViewAllDogsitAdapter(this@ViewAllDogsitActivity, viewType)
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


    private fun callDogsittingAPI() {
        dogsittingmodel.getAllDogsittingList(
            MyApp.getSharedPref().userId,viewType, MyApp.getSharedPref().userLat,
            MyApp.getSharedPref().userLong)
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

                        renderValues(it.data.alldogsitpetlist)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun renderValues(alldogsitpetlist: List<ViewAllDogsitPetList>) {
        viewAllAdapter.addData(alldogsitpetlist)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
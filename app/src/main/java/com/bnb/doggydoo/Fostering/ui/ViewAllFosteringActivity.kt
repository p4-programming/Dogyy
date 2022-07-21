package com.bnb.doggydoo.fostering.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityViewallBinding
import com.bnb.doggydoo.fostering.adapter.ViewAllFosteringAdapter
import com.bnb.doggydoo.fostering.datasource.model.NearByFosterPetList
import com.bnb.doggydoo.fostering.viewmodel.FosteringModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllFosteringActivity : AppCompatActivity() {
    private var _binding: ActivityViewallBinding? = null
    private val binding get() = _binding!!
    private var viewType: String =""
    private lateinit var fosteringmodel: FosteringModel
    private lateinit var viewAllAdapter: ViewAllFosteringAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewallBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(binding.root)
        getInit()
        callFosterViewAallAPI()
    }

    private fun getInit() {
        fosteringmodel = ViewModelProvider(this).get(FosteringModel::class.java)
        viewType = intent.getStringExtra("viewType").toString()
        //Toast.makeText(this, viewType, Toast.LENGTH_SHORT).show()
        viewAllAdapter = ViewAllFosteringAdapter(this@ViewAllFosteringActivity,viewType)
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

    private fun callFosterViewAallAPI() {
        fosteringmodel.getAllFosteringRequest(MyApp.getSharedPref().userId,viewType,MyApp.getSharedPref().userLat, MyApp.getSharedPref().userLong).observe(this, Observer {
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
                    renderList(it.data.allPetlist)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }

    private fun renderList(allPetlist: List<NearByFosterPetList>) {
        viewAllAdapter.addData(allPetlist)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
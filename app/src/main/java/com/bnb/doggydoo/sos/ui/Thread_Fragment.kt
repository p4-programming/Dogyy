package com.bnb.doggydoo.sos.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentThreadBinding
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ThreadFragment : Fragment() {
        private var _binding: FragmentThreadBinding? = null
        private val binding get() = _binding!!
        private lateinit var myDogViewModel: MyDogViewModel
        private var discription: String = ""
        private var dogimgs: String = ""
        private var cdate: String = ""
        private lateinit var recyclerAdapter: RecyclerAdapter
        private lateinit var DataList: ArrayList<getDistressPinByUserID.Datum>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentThreadBinding.inflate(layoutInflater)
            setHasOptionsMenu(true)
            return binding.root
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        getInit()
        callGetDogDescriptionAPI()


        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        DataList = ArrayList()
        }

        private fun getInit() {
            myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
    }

        @SuppressLint("FragmentLiveDataObserve")
        private fun callGetDogDescriptionAPI(){
            myDogViewModel.getPetDescriptionDatas(MyApp.getSharedPref().userId).observe(this, Observer {
                when(it.status){
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")){
                            it.data?.responseMessage?.snack(Color.RED,binding.parent)
                            return@Observer
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            if (it.data!=null){
                                Dataset(it.data.data)
                            }
                        }
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
        }


    private fun Dataset(sdata: MutableList<getDistressPinByUserID.Datum>){
        DataList.clear()
        discription = sdata[0].petDescription
        cdate = sdata[0].createdDate
        dogimgs = sdata[0].petImage
        DataList.addAll(sdata)
        recyclerAdapter = RecyclerAdapter(context!!,DataList)
        binding.recyclerview.adapter = recyclerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

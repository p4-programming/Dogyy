package com.bnb.doggydoo.sos.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentThreadBinding
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Thread_Fragment : Fragment() {
        private var _binding: FragmentThreadBinding? = null
        private val binding get() = _binding!!
        private lateinit var myDogViewModel: MyDogViewModel
        private var discription: String = ""
        private var dogimgs: String = ""
        private var cdate:String = ""
        private lateinit var recyclerAdapter: RecyclerAdapter
        private lateinit var DataList: ArrayList<getDistressPinByUserID.Datum>
        private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            // Inflate the layout for this fragment
            _binding = FragmentThreadBinding.inflate(layoutInflater)
            setHasOptionsMenu(true)

            getInit()
            callGetDogDescriptionAPI()

            binding.recyclerview.setHasFixedSize(true)
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            DataList = ArrayList()


            return binding.root
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        }

        private fun getInit() {
            myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
    }


        private fun callGetDogDescriptionAPI(){
            myDogViewModel.getPetDescriptionDatas(MyApp.getSharedPref().userId).observe(viewLifecycleOwner, Observer {
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
//        Log.i("TAG", "Hii : "+sdata[0].petImage)

//        imgs = Glide.with(context).load("https://doggydoo.in/assets/uploads/pets/" +sdata[0].petImage).toString().toInt()

        //DataList.add(DataClass(imgs,discription,cdate))
        DataList.addAll(sdata)
        recyclerAdapter = RecyclerAdapter(this,DataList)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
    }
}
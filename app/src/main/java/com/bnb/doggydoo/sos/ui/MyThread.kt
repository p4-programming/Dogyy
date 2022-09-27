package com.bnb.doggydoo.sos.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityMyThreadBinding
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyThread : AppCompatActivity() {
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var DataList: ArrayList<getDistressPinByUserID.Datum>//copy o
    private lateinit var recyclerView: RecyclerView

    private var binding:ActivityMyThreadBinding? = null
    private val bind get() = binding!!
    private lateinit var myDogViewModel: MyDogViewModel
    private var discription: String = ""
    private var dogimgs: String = ""
    private var imgs: Int = 0
    private var cdate:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyThreadBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(bind.root)

        getInit()
        callGetDogDescriptionAPI()

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        DataList = ArrayList()

    }

        private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)

        bind.ivBack.setOnClickListener {
            finish()
        }
    }


    private fun callGetDogDescriptionAPI(){
        myDogViewModel.getPetDescriptionDatas(MyApp.getSharedPref().userId).observe(this, Observer {
            when(it.status){
                Result.Status.LOADING ->{
                    bind.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    bind.progressBar.hide()
                    if (it.data?.responseCode.equals("0")){
                        it.data?.responseMessage?.snack(
                            Color.RED,bind.parent
                        )
                        return@Observer
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        if(it.data!=null){
                            Dataset(it.data.data)
                        }
                    }
                }
                Result.Status.ERROR -> {
                    bind.progressBar.hide()
                    it.message?.snack(Color.RED, bind.parent)
                }
            }
        })
    }


    private fun Dataset(sdata: MutableList<getDistressPinByUserID.Datum>){
        DataList.clear()
        discription = sdata[0].petDescription
        cdate = sdata[0].createdDate

//        imgs = Glide.with(context)
//            .load("https://doggydoo.in/assets/uploads/pets/" + sdata[0].petImage)
//            .error(R.drawable.alert)
//            .diskCacheStrategy(DiskCacheStrategy.ALL).toString().toInt()


        Log.d("Deepak","Description : $discription")
        Log.d("Deepak","Cdate : $cdate")
        Log.d("Deepak","PetImages : $imgs")

        //DataList.add(DataClass(imgs,discription,cdate))
        DataList.addAll(sdata)
        recyclerAdapter = RecyclerAdapter(this,DataList)
        recyclerView.adapter = recyclerAdapter
    }


    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
        Log.d("Deepak1","Resume")
    }
}
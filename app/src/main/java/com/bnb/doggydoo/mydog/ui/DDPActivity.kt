package com.bnb.doggydoo.mydog.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityDdpactivityBinding
import com.bnb.doggydoo.mydog.adapter.CommentsAdapter
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.DistressPetResponse
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.newsfeed.adapter.UserAdapter
import com.bnb.doggydoo.newsfeed.ui.CommentActivity
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.sos.ui.CommentsFragment
import com.bnb.doggydoo.sos.ui.adapter.CommentAdapter
import com.bnb.doggydoo.sos.ui.adapter.RecyclerAdapter
import com.bnb.doggydoo.sos.ui.model.CommentModels
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.bnb.doggydoo.videocall.ApplicationController.context
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.EnumSet.of
import java.util.List.of
import java.util.Set.of

@AndroidEntryPoint
class DDPActivity : AppCompatActivity() {

    private  var binding: ActivityDdpactivityBinding? = null
    private val bind get() = binding!!
    private lateinit var myDogViewModel: MyDogViewModel
    private lateinit var DataList: ArrayList<CommentModels>
    private var petId: String = ""
    private var userId: String = ""
    private var petImage: String = ""
    private var rvComents: RecyclerView ?= null
    private lateinit var mAdapter:CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDdpactivityBinding.inflate(layoutInflater)
        setContentView(bind.root)
        rvComents = findViewById(R.id.rvComments)
        //  ivsend = findViewById<ImageView>(R.id.sendcmt)
        getInit()
        callGetDogDescriptionAPI()
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        petId = intent.getStringExtra("petId").toString()

        bind.ivBack.setOnClickListener {
            finish()
        }

        bind.resolved.setOnClickListener {
            startActivity(Intent(applicationContext, MarkResolved::class.java))
        }

        //  bind.comments.setOnClickListener {
        //     ReadAndWriteCommment()
        //  }
    }

    private fun callGetDogDescriptionAPI() {
        myDogViewModel.getDistressPetDetailData(petId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        bind.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        bind.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                bind.parent
                            )
                            return@Observer
                        }
                        setDataInUI(it.data!!)
                    }
                    Result.Status.ERROR -> {
                        bind.progressBar.hide()
                        it.message?.snack(Color.RED, bind.parent)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(data: DistressPetResponse) {
        bind.tvdiscription.text = data.distressPetdetail.pet_description

        if (data.distressPetdetail.petImage.isNotEmpty()){
            petImage = ApiConstant.PET_IMAGE_BASE_URL + data.distressPetdetail.petImage[0]
            Log.d("Deepak","PetImage $petImage")
            bind.dogimg.loadImageFromString(this,petImage)
            bind.dogimg.show()
        }else{
            Log.d("Deepak","Else of image")
            Glide.with(this).load(R.drawable.dummy_pet).into(bind.dogimg)
        }
    }

//    private fun ReadAndWriteCommment() {
//        val dialog = Dialog(this@DDPActivity, R.style.Theme_Dialog)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.add_comments)
//        DataList = arrayListOf()
//  //      rvComents!!.layoutManager = LinearLayoutManager(context)
////        rvComents!!.setHasFixedSize(true)

//        //ivsend = findViewById(R.id.sendcmt)
//        //        cmt = findViewById(R.id.etComments)

////        getData()
////        setData()
//        dialog.show()
//    }

//    private fun setData(){
//        val rootRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("comments")
//        val demoref:DatabaseReference = rootRef.child(petId)

//        ivsend.setOnClickListener {
//            val value:String = cmt!!.text.toString()
//            Log.d("Deepak","Value : $value")
//            demoref.setValue(value)
//        }
//    }

//    private fun getData(){
//        FirebaseDatabase.getInstance().getReference("comments").child("152").addValueEventListener(
//            object :ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.i("TAG", "onDataChange: TEST :: $snapshot")
//                    DataList.clear()
//                    if (snapshot.exists()){
//                        Log.d("Deepak","Snapshot : $snapshot")
//                        for (userSnapshot in snapshot.children){
//                            Log.d("Deepak","usersnapshot : $userSnapshot")
//                            val user = userSnapshot.getValue(CommentModels::class.java)
//                            DataList.add(user!!)
//                            Log.d("Deepak","DataList : ${ DataList.add(user!!)}")
//                        }
//                        mAdapter = CommentsAdapter(context,DataList)
//                        rvComents!!.adapter = mAdapter
//                    }
//                }

//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(context,"Error $error",Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }

    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
    }
}

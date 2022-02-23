package com.aks.doggydoo.onboarding.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityChooseUseridBinding
import com.aks.doggydoo.newsfeed.util.RecyclerTouchListener
import com.aks.doggydoo.onboarding.adapter.UserIdSuggestionAdapter
import com.aks.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseUserIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseUseridBinding
    var name:String =""
    var finalUserName: String =""
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private lateinit var adapter: UserIdSuggestionAdapter
    private var userName = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseUseridBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()

        binding.button.setOnClickListener{
            startActivity(Intent(this, UploadUserPhotoActivity::class.java)
                .putExtra("name",name)
                .putExtra("username", finalUserName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }

    }

    private fun getInit() {
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)
        name = intent.getStringExtra("name")!!
        binding.gifImageView.loadImageAsGif(this, R.raw.onboard_look_up_down)
        binding.tvUserName.text = name
        finalUserName = name.replace(" ","")
        binding.tvUserId.text = "@"+ finalUserName

        adapter = UserIdSuggestionAdapter(this)
        binding.rvUserId.adapter = adapter
        binding.rvUserId.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.rvUserId,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        finalUserName = userName.get(position)
                        binding.tvUserId.text = "@"+finalUserName
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        getUserNameData()
    }


    private fun getUserNameData() {
        onBoardingViewModel.getUserName(binding.tvUserId.text.toString(),MyApp.getSharedPref().userId).
        observe(this@ChooseUserIdActivity, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    //set adapter
                    if (it.data.userNameList.size > 0){
                        binding.tvAvailable.visibility = View.GONE
                        binding.progressBar.hide()
                        for (category in it.data.userNameList) {
                            userName.add(category.user_name)
                        }
                        adapter.submitList(it.data.userNameList)
                    }else{
                        binding.tvAvailable.visibility = View.VISIBLE
                        binding.progressBar.hide()
                    }

                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}
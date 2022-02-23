package com.aks.doggydoo.homemodule.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentNotificationBinding
import com.aks.doggydoo.homemodule.datasource.model.notification.NotificationStatusList
import com.aks.doggydoo.homemodule.viewmodel.NotificationModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFrag : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding
    private val notificationModel: NotificationModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        binding.requestCheck.setOnCheckedChangeListener { _, isChecked ->
            postNotificationData(isChecked, "request")
        }
        binding.callCheck.setOnCheckedChangeListener { _, isChecked ->
            postNotificationData(isChecked, "message_call")
        }
        binding.newsCheck.setOnCheckedChangeListener { _, isChecked ->
            postNotificationData(isChecked, "newsfeed")
        }
        binding.trackCheck.setOnCheckedChangeListener { _, isChecked ->
            postNotificationData(isChecked, "tracking")
        }
        getNotificationStatusAPI()
    }

    private fun postNotificationData(isChecked: Boolean, type: String) {
        var stats="0"
        if (isChecked){
            stats="1"
        }
        notificationModel.postNotificationStatus(MyApp.getSharedPref().userId, type, stats)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Result.Status.LOADING -> {

                        }
                        Result.Status.SUCCESS -> {
                            if (it.data?.responseCode.equals("0")) {
                                it.data?.responseMessage?.snack(Color.RED, binding.parent)
                                return@Observer
                            }
                        }
                        Result.Status.ERROR -> {
                            it.message?.snack(Color.RED, binding.parent)
                        }
                    }
                })
    }

    private fun getNotificationStatusAPI() {
        notificationModel.getNotificationStatusResponse(MyApp.getSharedPref().userId).observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        binding.progressBar.hide()
                        setDataInUI(it.data!!.notificationStatusList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun setDataInUI(notificationStatusList: List<NotificationStatusList>) {
        binding.requestCheck.isChecked = notificationStatusList.get(0).request == "1"
        binding.callCheck.isChecked = notificationStatusList.get(0).message_call == "1"
        binding.newsCheck.isChecked = notificationStatusList.get(0).newsfeed == "1"
        binding.trackCheck.isChecked = notificationStatusList.get(0).tracking == "1"
    }
}
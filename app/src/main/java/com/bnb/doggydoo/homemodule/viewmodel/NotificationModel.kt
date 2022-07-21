package com.bnb.doggydoo.homemodule.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.homemodule.datasource.repo.notification.NotificationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationModel @Inject constructor(private var notificationRepo: NotificationRepo) :
    ViewModel() {

    fun getNotificationStatusResponse(userId: String) =
        notificationRepo.getParkDescriptionLiveData(userId)

    fun postNotificationStatus(userId: String, type: String, status: String) =
        notificationRepo.getPostNotificationResponse(userId, type, status)
}
package com.bnb.doggydoo.homemodule.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.homemodule.datasource.repo.home.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var homeRepo: HomeRepo) :
    ViewModel() {

    fun getUpcomingPlayDateResponse(
        userId: String, lattitute: String,
        longitute: String
    ) =
        homeRepo.getUpcomingPlayDateLiveData(userId, lattitute, longitute)

    fun getHomeMapResponse(
        userId: String, lattitute: String,
        longitute: String, type: String
    ) =
        homeRepo.getMapLiveData(userId, lattitute, longitute, type)

    fun getUserStatusResponse(user_id: String) =
        homeRepo.getUserStatusLiveData(user_id)

    fun getUserUpcomingResponse(user_id: String) =
        homeRepo.getUserUpcomingLiveData(user_id)

    fun getAllFriendResponse(user_id: String) =
        homeRepo.getFriendReqLiveData(user_id)

    fun AcceptRejectFriend(
        Request_id: String,
        request_status: String
    ) =
        homeRepo.getFriendActionLiveData(Request_id, request_status)

    fun getAllUser(user_id: String) =
        homeRepo.getAllUserReqLiveData(user_id)


    fun getAllUserReminder(user_id: String, type: String) =
        homeRepo.getAllReminderLiveData(user_id, type)

    fun getToken(request_id: String,type: String) =
        homeRepo.getTokenLiveData(request_id,type)

    fun getGenToken(
        user_name: String,
        refrence_id: String,
        room_id: String,
        user_id: String,
        type: String,
        notify_id:String
    ) =
        homeRepo.getTokenGenLiveData(user_name, refrence_id, room_id, user_id,type,notify_id)

}
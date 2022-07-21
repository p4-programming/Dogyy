package com.bnb.doggydoo.homemodule.datasource.repo.home

import com.bnb.doggydoo.utils.helper.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepo @Inject constructor(private var remoteHomeDataSource: RemoteHomeDataSource)
{
    fun getUpcomingPlayDateLiveData(
        userId: String,
        lattitute: String,
        longitute: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchUpcomingPlaydateResponse(userId,lattitute,longitute) }
    )


    fun getMapLiveData(
        userId: String,
        lattitute: String,
        longitute: String,
        type: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchMapResponse(userId,lattitute,longitute,type) }
    )

    fun getUserStatusLiveData(
        user_id: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchUserStatusResponse(user_id) }
    )


    fun getUserUpcomingLiveData(
        user_id: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchUserUpcomingResponse(user_id) }
    )

    fun getFriendReqLiveData(
        user_id: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchAllFriendReqResponse(user_id) }
    )

    fun getFriendActionLiveData(
        Request_id: String,
        request_status: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchAcceptRejectFriendResponse(Request_id, request_status) }
    )

    fun getAllUserReqLiveData(
        user_id: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchAllUserResponse(user_id) }
    )

    fun getAllReminderLiveData(
        user_id: String,
        type: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchAllReminder(user_id, type) }
    )


    fun getTokenLiveData(
        request_id: String,
        type: String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchToken(request_id,type) }
    )

    fun getTokenGenLiveData(
        user_name:String,
        refrence_id:String,
        room_id: String,
        user_id: String,
        type: String,
        notify_id:String
    ) = resultLiveData(
        networkCall = { remoteHomeDataSource.fetchGenToken(user_name,refrence_id,room_id,user_id,type,notify_id) }
    )
}
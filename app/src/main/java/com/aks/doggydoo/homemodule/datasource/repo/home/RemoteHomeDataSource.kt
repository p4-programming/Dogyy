package com.aks.doggydoo.homemodule.datasource.repo.home

import com.aks.doggydoo.utils.network.ApiFactory
import com.aks.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteHomeDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {
    /**
     * fetch Upcoming Playdate Response from network
     * */
    suspend fun fetchUpcomingPlaydateResponse(
        user_id: String,
        lattitute: String,
        longitute: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getUpcomingPlayDate(
                    user_id = user_id,
                    lattitute = lattitute,
                    longitute= longitute
                )
        }


    suspend fun fetchMapResponse(
        user_id: String,
        lattitute: String,
        longitute: String,
        type: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getMapLocation(
                    user_id = user_id,
                    lattitute = lattitute,
                    longitute= longitute,
                    type= type
                )
        }

    suspend fun fetchUserStatusResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getUserStatus(
                    user_id = user_id
                )
        }


    suspend fun fetchUserUpcomingResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getUserUpcoming(
                    user_id = user_id
                )
        }

    suspend fun fetchAllFriendReqResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getAllFriendReq(
                    user_id = user_id
                )
        }

    suspend fun fetchAcceptRejectFriendResponse(
        Request_id: String,
        request_status: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .acceptRejectFriend(
                    Request_id, request_status
                )
        }

    suspend fun fetchAllUserResponse(
        user_id: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getAllUserList(
                  user_id
                )
        }


    suspend fun fetchAllReminder(
        user_id: String,
        type: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getAllReminder(
                    user_id, type
                )
        }

    suspend fun fetchToken(
        request_id: String,
        type: String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getToken(
                    request_id,type
                )
        }


    suspend fun fetchGenToken(
        user_name:String,
        refrence_id:String,
        room_id: String,
        user_id: String,
        type: String,
        notify_id:String
    ) =
        getResult {
            apiFactory.createService(HomeApiService::class.java)
                .getCreatedToken(
                    user_name,refrence_id,room_id,user_id,type,notify_id
                )
        }

}
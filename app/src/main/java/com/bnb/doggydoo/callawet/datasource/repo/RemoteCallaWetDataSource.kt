package com.bnb.doggydoo.callawet.datasource.repo

import com.bnb.doggydoo.callawet.datasource.repo.call.CallApiService
import com.bnb.doggydoo.callawet.datasource.repo.callreason.CallReasonApiService
import com.bnb.doggydoo.callawet.datasource.repo.home.CallawetHomeApiService
import com.bnb.doggydoo.callawet.datasource.repo.nearbydoc.NearByDoctorDetailsApiService
import com.bnb.doggydoo.callawet.datasource.repo.nearbyhos.NearByHospitalApiService
import com.bnb.doggydoo.utils.network.ApiFactory
import com.bnb.doggydoo.utils.network.ResponseHandler
import javax.inject.Inject

class RemoteCallaWetDataSource @Inject constructor(private val apiFactory: ApiFactory) :
    ResponseHandler() {

    suspend fun fetchCallaWetResponse(
        userid: String,
        latitude:String,
        longitude:String
    ) =
        getResult {
            apiFactory.createService(CallawetHomeApiService::class.java)
                .getCallawetHomeDataList(
                    userid,latitude,longitude
                )
        }


    suspend fun fetchAllClinicResponse(
        userid: String
    ) =
        getResult {
            apiFactory.createService(CallawetHomeApiService::class.java)
                .getAllClinic(
                    userid
                )
        }


    suspend fun fetchAllDocResponse(
        userid: String
    ) =
        getResult {
            apiFactory.createService(CallawetHomeApiService::class.java)
                .getAllDoctor(
                    userid
                )
        }


    suspend fun fetchDocDetailResponse(
        vetid: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(NearByDoctorDetailsApiService::class.java)
                .getDoctorDetails(
                    vetid, user_id
                )
        }


    suspend fun fetchHosDetailResponse(
        clinic_id: String,
        user_id: String
    ) =
        getResult {
            apiFactory.createService(NearByHospitalApiService::class.java)
                .getNearbyHospitalDetails(
                    clinic_id, user_id
                )
        }

    suspend fun fetchCallReasonResponse() =
        getResult {
            apiFactory.createService(CallReasonApiService::class.java)
                .getCallReason(
                )
        }

    suspend fun fetchCall(
        user_id: String,
        doctor_id: String,
        reason: String,
        type: String
    ) =
        getResult {
            apiFactory.createService(CallApiService::class.java)
                .call(
                    user_id,doctor_id,reason,type
                )
        }

}
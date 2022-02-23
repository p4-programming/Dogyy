package com.aks.doggydoo.callawet.datasource.repo.callreason

import com.aks.doggydoo.callawet.datasource.model.callreason.CallReasonResponse
import retrofit2.Response
import retrofit2.http.POST

interface CallReasonApiService {
    @POST("VatApi/reasonacall")
    suspend fun getCallReason(): Response<CallReasonResponse>
}
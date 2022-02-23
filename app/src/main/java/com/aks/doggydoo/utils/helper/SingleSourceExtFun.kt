package com.aks.doggydoo.utils.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from database
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
fun <A> resultLiveData(
    networkCall: suspend () -> Result<A>
): LiveData<Result<A?>> = liveData(Dispatchers.IO) {
        emit(Result.loading<A>())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Result.Status.SUCCESS) {
            emit(Result.success(responseStatus.data))
            print(responseStatus.data)
        } else if (responseStatus.status == Result.Status.ERROR) {
            emit(Result.error<A>(responseStatus.message!!, null))
        }
    }
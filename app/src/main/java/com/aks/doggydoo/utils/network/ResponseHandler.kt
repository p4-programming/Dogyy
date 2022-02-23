package com.aks.doggydoo.utils.network

import retrofit2.Response
import com.aks.doggydoo.utils.helper.Result
/**
 * Abstract ResponseHandler class with error handling
 */
abstract class ResponseHandler {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(
                    body
                )
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return  Result.error(e.message?:e.toString())
        }
    }

}


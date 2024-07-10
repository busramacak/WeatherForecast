package com.bmprj.weatherforecast.util

import retrofit2.Response

sealed class ApiResources<out R> {
    data class Success<out R>(val result: R) : ApiResources<R>()
    data class Failure(val exception:RetrofitError) : ApiResources<Nothing>()
}

sealed class RetrofitError(val errorMessage: String? = null) {
    data class ServerError<out T>(val response: Response<@UnsafeVariance T>) : RetrofitError(response.message())
    class UnKnown : RetrofitError()
}
fun <T> handleResponse(
    isSuccessful: Boolean,
    response: Response<T>
): ApiResources<T> {

    val body = response.body()
    if (isSuccessful && response.errorBody() == null && body != null) { // success and have data
        return ApiResources.Success(body)
    }

    val error = if (isSuccessful && response.errorBody() != null) {// success and no data
        RetrofitError.ServerError(response) // handle error code
    } else {
        RetrofitError.UnKnown()
        // add logs
    }
    return ApiResources.Failure(error)
}
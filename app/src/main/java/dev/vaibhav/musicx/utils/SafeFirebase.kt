package dev.vaibhav.musicx.utils

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeCall(
    handleNullCheck: Boolean = true,
    call: suspend () -> T
): Resource<T> = try {
    val response = call()
    if (handleNullCheck) handleNullCheck(response)
    else Resource.Success(data = response)
} catch (e: IOException) {
    Resource.Error(ErrorType.NO_INTERNET)
} catch (e: Exception) {
    Resource.Error(ErrorType.UNKNOWN, message = e.message.toString())
}

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): Resource<T> = try {
    val response = call()
    if (response.isSuccessful)
        response.body()?.let { Resource.Success(data = it) }
            ?: Resource.Error(message = "Data is null")
    else
        Resource.Error(message = response.message())
} catch (e: IOException) {
    Resource.Error(ErrorType.NO_INTERNET)
} catch (e: Exception) {
    Resource.Error(ErrorType.UNKNOWN, message = e.message.toString())
}

fun <T> handleNullCheck(data: T?): Resource<T> = data?.let {
    Resource.Success(data = it)
} ?: Resource.Error(message = "Data is null")

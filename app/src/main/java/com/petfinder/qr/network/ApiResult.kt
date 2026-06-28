package com.petfinder.qr.network

/** Typed outcome of a network call, used for explicit error handling. */
sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val error: ApiError) : ApiResult<Nothing>
}

/** Categorised network failure with a user-presentable message. */
sealed interface ApiError {
    data class Http(val code: Int, val serverMessage: String?) : ApiError
    data object Network : ApiError
    data object Unauthorized : ApiError
    data class Unknown(val cause: String?) : ApiError

    fun userMessage(): String = when (this) {
        is Http -> when (code) {
            in 500..599 -> "The server had a problem. Please try again."
            404 -> "Not found."
            else -> serverMessage ?: "Request failed ($code)."
        }
        Network -> "No connection. Please check your network."
        Unauthorized -> "Your session expired. Please sign in again."
        is Unknown -> cause ?: "Something went wrong."
    }
}

inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) block(data)
    return this
}

inline fun <T> ApiResult<T>.onError(block: (ApiError) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) block(error)
    return this
}

fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data

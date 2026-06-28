package com.petfinder.qr.network

import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

/**
 * Wraps a network call, mapping exceptions to a typed [ApiResult] so callers
 * never deal with raw throwables.
 */
suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> = try {
    ApiResult.Success(block())
} catch (e: HttpException) {
    val error = if (e.code() == 401 || e.code() == 403) {
        ApiError.Unauthorized
    } else {
        ApiError.Http(e.code(), e.message())
    }
    ApiResult.Error(error)
} catch (e: IOException) {
    ApiResult.Error(ApiError.Network)
} catch (e: Exception) {
    ApiResult.Error(ApiError.Unknown(e.message))
}

/**
 * Retries [block] with exponential backoff on transient failures (connectivity
 * drops and 5xx responses). Client errors (4xx) fail fast — retrying won't help.
 * Re-throws the final exception so it can be handled by [safeApiCall].
 */
suspend fun <T> retryIO(
    times: Int = 3,
    initialDelayMs: Long = 500,
    maxDelayMs: Long = 4_000,
    factor: Double = 2.0,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelayMs
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // transient — fall through to retry
        } catch (e: HttpException) {
            if (e.code() < 500) throw e // not retryable
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMs)
    }
    return block() // final attempt; let it throw
}

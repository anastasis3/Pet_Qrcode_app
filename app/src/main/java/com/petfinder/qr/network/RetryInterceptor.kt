package com.petfinder.qr.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Transport-level retry: re-issues a request a few times when the connection
 * itself fails (timeouts, resets). Application-level/backoff retries live in
 * [retryIO]; this just smooths over flaky connections.
 */
@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastError: IOException? = null

        while (attempt <= MAX_RETRIES) {
            try {
                return chain.proceed(request)
            } catch (e: IOException) {
                lastError = e
                attempt++
            }
        }
        throw lastError ?: IOException("Request failed after $MAX_RETRIES retries")
    }

    private companion object {
        const val MAX_RETRIES = 2
    }
}

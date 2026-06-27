package com.petfinder.qr.network

import android.util.Base64
import java.util.UUID

/**
 * Generates throwaway, well-formed JWT strings (header.payload.signature) for the
 * mock backend. The signature is random — these are NOT verifiable, they only
 * give the client realistic tokens to store and send until Spring Boot issues
 * real ones.
 */
object MockJwt {

    fun create(subject: String, ttlSeconds: Long): String {
        val now = System.currentTimeMillis() / 1000
        val header = """{"alg":"HS256","typ":"JWT"}"""
        val payload = """{"sub":"$subject","iat":$now,"exp":${now + ttlSeconds}}"""
        return "${encode(header)}.${encode(payload)}.${encode("mock-sig-${UUID.randomUUID()}")}"
    }

    private fun encode(value: String): String =
        Base64.encodeToString(
            value.toByteArray(Charsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP,
        )
}

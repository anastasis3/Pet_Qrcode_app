package com.petfinder.qr.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.petfinder.qr.network.dto.AuthResponse
import com.petfinder.qr.network.dto.UserDto
import com.petfinder.qr.utils.Constants
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stands in for the not-yet-built backend. While [Constants.USE_MOCK_AUTH] is on,
 * it answers `/api/auth/*` calls with synthetic JWT responses instead of hitting
 * the network. Removing it (or flipping the flag) hands the calls to the real
 * Spring Boot server transparently.
 */
@Singleton
class MockAuthInterceptor @Inject constructor() : Interceptor {

    private val gson = Gson()
    private val jsonMedia = "application/json".toMediaType()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (!Constants.USE_MOCK_AUTH || !path.contains("/auth/")) {
            return chain.proceed(request)
        }

        // Simulate a little network latency for realistic loading states.
        runCatching { Thread.sleep(600) }

        val body = readJsonBody(request)
        return when {
            path.endsWith("/auth/login") -> {
                val email = body["email"].orEmptyOr("owner@example.com")
                authResponse(request, name = email.substringBefore("@").replaceFirstChar { it.uppercase() }, email = email)
            }

            path.endsWith("/auth/register") -> {
                val email = body["email"].orEmptyOr("owner@example.com")
                val name = body["name"].orEmptyOr(email.substringBefore("@"))
                authResponse(request, name = name, email = email)
            }

            path.endsWith("/auth/refresh") ->
                authResponse(request, name = "Pet Owner", email = "owner@example.com")

            path.endsWith("/auth/logout") -> okJson(request, """{"success":true}""")

            else -> chain.proceed(request)
        }
    }

    private fun authResponse(request: Request, name: String, email: String): Response {
        val ttl = 3600L
        val payload = AuthResponse(
            accessToken = MockJwt.create(email, ttl),
            refreshToken = MockJwt.create("$email-refresh", ttl * 24 * 7),
            tokenType = "Bearer",
            expiresIn = ttl,
            user = UserDto(
                id = UUID.nameUUIDFromBytes(email.toByteArray()).toString(),
                name = name,
                email = email,
            ),
        )
        return okJson(request, gson.toJson(payload))
    }

    private fun okJson(request: Request, json: String): Response =
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .addHeader("content-type", "application/json")
            .body(json.toResponseBody(jsonMedia))
            .build()

    private fun readJsonBody(request: Request): Map<String, String> {
        val raw = runCatching {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            buffer.readUtf8()
        }.getOrNull()

        if (raw.isNullOrBlank()) return emptyMap()
        return runCatching {
            gson.fromJson(raw, JsonObject::class.java)
                .entrySet()
                .associate { (key, value) -> key to (if (value.isJsonNull) "" else value.asString) }
        }.getOrDefault(emptyMap())
    }

    private fun String?.orEmptyOr(fallback: String): String =
        this?.takeIf { it.isNotBlank() } ?: fallback
}

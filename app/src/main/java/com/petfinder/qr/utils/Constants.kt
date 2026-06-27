package com.petfinder.qr.utils

object Constants {
    /** Swap to the Spring Boot host when the backend is live. */
    const val BASE_URL = "https://api.petfinder.app/"
    const val QR_BASE_URL = "https://petfinder.app/pet/"

    /**
     * When true, [com.petfinder.qr.network.MockAuthInterceptor] short-circuits the
     * `/api/auth/*` calls with synthetic JWT responses. Set to false once the
     * Spring Boot backend is reachable — no other code needs to change.
     */
    const val USE_MOCK_AUTH = true
}

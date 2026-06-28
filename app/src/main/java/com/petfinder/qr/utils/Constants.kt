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

    /**
     * Master switch for the data layer.
     *  - false  -> offline/local only: Room is the source of truth and demo data
     *              is seeded (current behaviour; app runs with no server).
     *  - true   -> offline-first against the Spring Boot REST API: every screen
     *              reads the Room cache while the repository refreshes from the
     *              network and writes results back. No fake data is seeded.
     *
     * Flip to true (and point [BASE_URL] at the server) once the backend is live —
     * no UI/ViewModel changes are required.
     */
    const val USE_REMOTE_BACKEND = false
}

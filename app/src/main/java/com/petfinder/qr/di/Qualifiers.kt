package com.petfinder.qr.di

import javax.inject.Qualifier

/** A long-lived, application-scoped [kotlinx.coroutines.CoroutineScope] for fire-and-forget work (cache refreshes). */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

package com.petfinder.qr.di

import com.petfinder.qr.sync.LocalScanSyncService
import com.petfinder.qr.sync.RemoteScanSyncService
import com.petfinder.qr.sync.ScanSyncService
import com.petfinder.qr.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the active [ScanSyncService]: backend sync when remote is enabled,
 * otherwise a no-op. Controlled by [Constants.USE_REMOTE_BACKEND].
 */
@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideScanSyncService(
        local: LocalScanSyncService,
        remote: RemoteScanSyncService,
    ): ScanSyncService = if (Constants.USE_REMOTE_BACKEND) remote else local
}

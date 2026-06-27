package com.petfinder.qr.di

import com.petfinder.qr.sync.LocalScanSyncService
import com.petfinder.qr.sync.ScanSyncService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the active [ScanSyncService]. Swap [LocalScanSyncService] for
 * `RemoteScanSyncService` here once the backend sync endpoint exists.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

    @Binds
    @Singleton
    abstract fun bindScanSyncService(impl: LocalScanSyncService): ScanSyncService
}

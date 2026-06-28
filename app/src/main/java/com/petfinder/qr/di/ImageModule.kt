package com.petfinder.qr.di

import com.petfinder.qr.image.ApiImageUploader
import com.petfinder.qr.image.ImageUploader
import com.petfinder.qr.image.LocalImageUploader
import com.petfinder.qr.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the active [ImageUploader]: backend multipart upload when remote is
 * enabled, otherwise local-only. Controlled by [Constants.USE_REMOTE_BACKEND].
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideImageUploader(
        local: LocalImageUploader,
        api: ApiImageUploader,
    ): ImageUploader = if (Constants.USE_REMOTE_BACKEND) api else local
}

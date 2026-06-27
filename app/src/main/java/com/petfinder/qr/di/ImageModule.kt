package com.petfinder.qr.di

import com.petfinder.qr.image.ImageUploader
import com.petfinder.qr.image.LocalImageUploader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the active [ImageUploader]. Swap [LocalImageUploader] for
 * `R2ImageUploader` here once Cloudflare R2 is ready — nothing else changes.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    @Binds
    @Singleton
    abstract fun bindImageUploader(impl: LocalImageUploader): ImageUploader
}

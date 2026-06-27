package com.petfinder.qr.di

import android.content.Context
import androidx.room.Room
import com.petfinder.qr.database.PetFinderDatabase
import com.petfinder.qr.network.PetFinderApiService
import com.petfinder.qr.network.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitProvider.createRetrofit(okHttpClient)
    }

    @Provides
    @Singleton
    fun providePetFinderApiService(retrofit: Retrofit): PetFinderApiService {
        return retrofit.create(PetFinderApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePetFinderDatabase(
        @ApplicationContext context: Context,
    ): PetFinderDatabase {
        return Room.databaseBuilder(
            context,
            PetFinderDatabase::class.java,
            "petfinder_database",
        ).fallbackToDestructiveMigration()
            .build()
    }
}

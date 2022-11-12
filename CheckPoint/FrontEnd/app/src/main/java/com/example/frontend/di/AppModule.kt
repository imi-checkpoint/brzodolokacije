package com.example.frontend.di

import Constants.Companion.BASE_URL
import com.example.frontend.data.remote.CheckpointApi
import com.example.frontend.data.repository.CheckpointRepositoryImpl
import com.example.frontend.domain.repository.CheckpointRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCheckpointApi() : CheckpointApi {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            ))
            .build()
            .create(CheckpointApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCheckpointRepository(api : CheckpointApi) : CheckpointRepository {
        return CheckpointRepositoryImpl(api)
    }
}
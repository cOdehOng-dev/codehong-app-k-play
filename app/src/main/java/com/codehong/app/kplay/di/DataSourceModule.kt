package com.codehong.app.kplay.di

import com.codehong.app.kplay.data.datasource.PerformanceRemoteDataSource
import com.codehong.app.kplay.data.remote.KopisApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providePerformanceRemoteDataSource(
        kopisApiService: KopisApiService
    ) = PerformanceRemoteDataSource(kopisApiService)
}
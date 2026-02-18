package com.codehong.app.kplay.di

import com.codehong.app.kplay.data.datasource.PerformanceLocalDataSource
import com.codehong.app.kplay.data.datasource.PerformanceRemoteDataSource
import com.codehong.app.kplay.data.repository.PerformanceRepositoryImpl
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePerformanceRepository(
        remote: PerformanceRemoteDataSource,
        local: PerformanceLocalDataSource
    ): PerformanceRepository = PerformanceRepositoryImpl(remote, local)
}
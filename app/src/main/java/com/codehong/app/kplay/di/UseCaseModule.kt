package com.codehong.app.kplay.di

import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providePerformanceUseCase(
        repository: PerformanceRepository
    ) = PerformanceUseCase(repository)
}
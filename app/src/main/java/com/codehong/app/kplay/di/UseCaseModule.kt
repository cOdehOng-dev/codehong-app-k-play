package com.codehong.app.kplay.di

import com.codehong.app.kplay.domain.repository.FavoriteRepository
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.app.kplay.domain.usecase.FavoriteUseCase
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.kplay.domain.usecase.PlaceUseCase
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

    @Provides
    @Singleton
    fun providePlaceUseCase(
        repository: PerformanceRepository
    ) = PlaceUseCase(repository)

    @Provides
    @Singleton
    fun provideFavoriteUseCase(
        repository: FavoriteRepository
    ) = FavoriteUseCase(repository)
}
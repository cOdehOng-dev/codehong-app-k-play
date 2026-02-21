package com.codehong.app.kplay.di

import com.codehong.app.kplay.data.datasource.PerformanceLocalDataSource
import com.codehong.app.kplay.data.datasource.PerformanceRemoteDataSource
import com.codehong.app.kplay.data.room.dao.FavoritePerformanceDao
import com.codehong.app.kplay.data.room.dao.PlaceDetailDao
import com.codehong.app.kplay.data.repository.FavoriteRepositoryImpl
import com.codehong.app.kplay.data.repository.PerformanceRepositoryImpl
import com.codehong.app.kplay.data.repository.PlaceDetailCacheRepositoryImpl
import com.codehong.app.kplay.domain.repository.FavoriteRepository
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.app.kplay.domain.repository.PlaceDetailCacheRepository
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

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        dao: FavoritePerformanceDao
    ): FavoriteRepository = FavoriteRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePlaceDetailCacheRepository(
        dao: PlaceDetailDao
    ): PlaceDetailCacheRepository = PlaceDetailCacheRepositoryImpl(dao)
}
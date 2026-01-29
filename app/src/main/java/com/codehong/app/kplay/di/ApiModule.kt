package com.codehong.app.kplay.di

import com.codehong.app.kplay.data.remote.KopisApiService
import com.codehong.library.network.ConvertType
import com.codehong.library.network.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideKopisApiService(): KopisApiService {
        return NetworkManager.getApiService<KopisApiService>(
            baseUrl = "https://kopis.or.kr/",
            convertType = ConvertType.XML,
            converters = listOf(GsonConverterFactory.create())
        )
    }
}
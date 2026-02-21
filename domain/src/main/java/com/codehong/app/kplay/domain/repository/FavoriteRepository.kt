package com.codehong.app.kplay.domain.repository

import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteList(): Flow<List<FavoritePerformance>>
    suspend fun addFavorite(performance: FavoritePerformance)
    suspend fun removeFavorite(id: String)
    suspend fun isFavorite(id: String): Boolean
}

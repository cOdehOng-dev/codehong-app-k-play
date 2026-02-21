package com.codehong.app.kplay.domain.usecase

import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    fun getFavoriteList(): Flow<List<FavoritePerformance>> = repository.getFavoriteList()

    suspend fun addFavorite(performance: FavoritePerformance) = repository.addFavorite(performance)

    suspend fun removeFavorite(id: String) = repository.removeFavorite(id)

    suspend fun isFavorite(id: String): Boolean = repository.isFavorite(id)
}

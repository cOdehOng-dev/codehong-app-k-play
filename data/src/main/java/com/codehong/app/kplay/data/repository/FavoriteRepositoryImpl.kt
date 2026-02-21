package com.codehong.app.kplay.data.repository

import com.codehong.app.kplay.data.mapper.asDomain
import com.codehong.app.kplay.data.mapper.toEntity
import com.codehong.app.kplay.data.room.dao.FavoritePerformanceDao
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoritePerformanceDao
) : FavoriteRepository {

    override fun getFavoriteList(): Flow<List<FavoritePerformance>> =
        dao.getAllFavorites().map { entities ->
            entities.map { it.asDomain() }
        }

    override suspend fun addFavorite(performance: FavoritePerformance) {
        dao.insert(performance.toEntity())
    }

    override suspend fun removeFavorite(id: String) {
        dao.deleteById(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return dao.countById(id) > 0
    }
}

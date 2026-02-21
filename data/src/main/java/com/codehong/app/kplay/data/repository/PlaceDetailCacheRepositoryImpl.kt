package com.codehong.app.kplay.data.repository

import com.codehong.app.kplay.data.mapper.asDomain
import com.codehong.app.kplay.data.mapper.toEntity
import com.codehong.app.kplay.data.room.dao.PlaceDetailDao
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.repository.PlaceDetailCacheRepository

class PlaceDetailCacheRepositoryImpl(
    private val dao: PlaceDetailDao
) : PlaceDetailCacheRepository {

    override suspend fun getPlaceDetail(keyword: String): PlaceDetail? {
        val entity = dao.getByKeyword(keyword) ?: return null
        // 위경도가 없으면 캐시 미스로 처리
        if (entity.latitude.isNullOrBlank() || entity.longitude.isNullOrBlank()) return null
        return entity.asDomain()
    }

    override suspend fun savePlaceDetail(keyword: String, placeDetail: PlaceDetail) {
        dao.insert(placeDetail.toEntity(keyword))
    }

    override suspend fun getCacheSizeBytes(): Long {
        return dao.getSizeBytes() ?: 0L
    }

    override suspend fun clearCache() {
        dao.clearAll()
    }
}

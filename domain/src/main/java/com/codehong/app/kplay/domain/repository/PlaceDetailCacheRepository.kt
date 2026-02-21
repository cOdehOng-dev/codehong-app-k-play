package com.codehong.app.kplay.domain.repository

import com.codehong.app.kplay.domain.model.place.PlaceDetail

interface PlaceDetailCacheRepository {
    suspend fun getPlaceDetail(keyword: String): PlaceDetail?
    suspend fun savePlaceDetail(keyword: String, placeDetail: PlaceDetail)
    suspend fun getCacheSizeBytes(): Long
    suspend fun clearCache()
}

package com.codehong.app.kplay.domain.usecase

import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.model.place.PlaceInfoItem
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.app.kplay.domain.repository.PlaceDetailCacheRepository
import com.codehong.library.network.CallStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceUseCase @Inject constructor(
    private val repository: PerformanceRepository,
    private val cacheRepository: PlaceDetailCacheRepository
) {

    fun searchPlace(
        serviceKey: String,
        keyword: String,
        currentPage: String,
        rowsPerPage: String
    ): Flow<List<PlaceInfoItem>?> = flow {
        repository.searchPlace(serviceKey, keyword, currentPage, rowsPerPage).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> {
                    emit(status.responseData)
                }
                else -> emit(null)
            }
        }
    }

    suspend fun getCacheSize(): Long = cacheRepository.getCacheSizeBytes()

    suspend fun clearCache() = cacheRepository.clearCache()

    fun getPlaceDetail(
        serviceKey: String,
        keyword: String,
        currentPage: String,
        rowsPerPage: String
    ): Flow<PlaceDetail?> = flow {
        // 1. Room 캐시 확인
        val cached = cacheRepository.getPlaceDetail(keyword)
        if (cached != null) {
            emit(cached)
            return@flow
        }

        // 2. 캐시 없으면 API 호출
        searchPlace(
            serviceKey = serviceKey,
            keyword = keyword,
            currentPage = currentPage,
            rowsPerPage = rowsPerPage
        ).collect {
            if (it.isNullOrEmpty()) {
                emit(null)
            } else {
                val placeId = it.find { placeInfo -> keyword.contains(placeInfo.placeName ?: "") }?.placeId
                if (placeId.isNullOrEmpty()) {
                    emit(null)
                } else {
                    repository.getPlaceDetail(serviceKey, placeId).collect { detailStatus ->
                        when (detailStatus) {
                            is CallStatus.Loading -> {}
                            is CallStatus.Success -> {
                                val detail = detailStatus.responseData
                                // 3. API 결과를 Room에 저장
                                if (detail != null) {
                                    cacheRepository.savePlaceDetail(keyword, detail)
                                }
                                emit(detail)
                            }
                            else -> emit(null)
                        }
                    }
                }
            }
        }
    }
}

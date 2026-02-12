package com.codehong.app.kplay.domain.usecase

import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.model.place.PlaceInfoItem
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.library.network.CallStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceUseCase @Inject constructor(
    private val repository: PerformanceRepository
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
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }

    fun getPlaceDetail(
        serviceKey: String,
        id: String
    ): Flow<PlaceDetail?> = flow {
        repository.getPlaceDetail(serviceKey, id).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }
}
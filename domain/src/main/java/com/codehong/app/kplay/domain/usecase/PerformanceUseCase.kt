package com.codehong.app.kplay.domain.usecase

import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.CallStatus
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PerformanceUseCase @Inject constructor(
    private val repository: PerformanceRepository
) {

    fun getPerformanceList(
        service: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String? = null,
        signGuCode: String? = null,
        signGuCodeSub: String? = null,
        kidState: String? = null,
        genreCode: String? = null
    ): Flow<List<PerformanceInfoItem>?> = flow {
        repository.getPerformanceList(
            service,
            startDate,
            endDate,
            currentPage,
            rowsPerPage,
            performanceState,
            signGuCode,
            signGuCodeSub,
            kidState,
            genreCode
        ).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }

    fun getPerformanceDetail(
        serviceKey: String,
        id: String
    ): Flow<List<PerformanceDetail>?> = flow {
        repository.getPerformanceDetail(serviceKey, id).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }

    fun getRankList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        genreCode: String? = null,
        area: String? = null
    ): Flow<List<BoxOfficeItem>?> = flow {
        repository.getRankList(serviceKey, startDate, endDate, genreCode, area).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }

    fun getFestivalList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String
    ): Flow<List<PerformanceInfoItem>?> = flow {
        repository.getFestivalList(
            serviceKey,
            startDate,
            endDate,
            currentPage,
            rowsPerPage
        ).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }
}
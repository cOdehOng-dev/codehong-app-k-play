package com.codehong.app.kplay.domain.usecase

import com.codehong.app.kplay.domain.model.CallStatus
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PerformanceUseCase @Inject constructor(
    private val repository: PerformanceRepository
) {

    fun getPerformanceList(
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String? = null,
        cityCode: String? = null,
        signGuCodeSub: String? = null,
        kidState: String? = null
    ): Flow<List<PerformanceInfoItem>?> = flow {
        repository.getPerformanceList(
            startDate,
            endDate,
            currentPage,
            rowsPerPage,
            performanceState,
            cityCode,
            signGuCodeSub,
            kidState
        ).collect { status ->
            when (status) {
                is CallStatus.Loading -> {}
                is CallStatus.Success -> emit(status.responseData)
                else -> emit(null)
            }
        }
    }
}
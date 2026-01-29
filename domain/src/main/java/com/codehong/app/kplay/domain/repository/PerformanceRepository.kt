package com.codehong.app.kplay.domain.repository

import com.codehong.app.kplay.domain.model.CallStatus
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {

    fun getPerformanceList(
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String? = null,
        cityCode: String? = null,
        signGuCodeSub: String? = null,
        kidState: String? = null
    ): Flow<CallStatus<List<PerformanceInfoItem>?>>
}
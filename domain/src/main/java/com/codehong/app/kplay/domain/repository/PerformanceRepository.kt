package com.codehong.app.kplay.domain.repository

import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.CallStatus
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {

    fun getPerformanceList(
        service: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String? = null,
        signGuCode: String? = null,
        signGuCodeSub: String? = null,
        kidState: String? = null
    ): Flow<CallStatus<List<PerformanceInfoItem>?>>

    fun getPerformanceDetail(
        serviceKey: String,
        id: String
    ): Flow<CallStatus<List<PerformanceDetail>?>>

    fun getRankList(
        serviceKey: String,
        startDate: String,
        endDate: String
    ): Flow<CallStatus<List<BoxOfficeItem>?>>
}
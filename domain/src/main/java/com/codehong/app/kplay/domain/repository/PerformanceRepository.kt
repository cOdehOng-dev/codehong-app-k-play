package com.codehong.app.kplay.domain.repository

import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.model.place.PlaceInfoItem
import com.codehong.library.network.CallStatus
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
        kidState: String? = null,
        genreCode: String? = null
    ): Flow<CallStatus<List<PerformanceInfoItem>?>>

    fun getPerformanceDetail(
        serviceKey: String,
        id: String
    ): Flow<CallStatus<List<PerformanceDetail>?>>

    fun getRankList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        genreCode: String?,
        area: String?
    ): Flow<CallStatus<List<BoxOfficeItem>?>>

    fun getFestivalList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String?,
        signGuCodeSub: String?
    ): Flow<CallStatus<List<PerformanceInfoItem>?>>

    fun getAwardedPerformanceList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String? = null,
        signGuCodeSub: String? = null
    ): Flow<CallStatus<List<PerformanceInfoItem>?>>

    fun searchPlace(
        serviceKey: String,
        keyword: String,
        currentPage: String,
        rowsPerPage: String
    ): Flow<CallStatus<List<PlaceInfoItem>?>>

    fun getPlaceDetail(
        serviceKey: String,
        id: String
    ) : Flow<CallStatus<PlaceDetail?>>

    fun setMyLocation(
        myLocation: String?
    )

    fun getMyLocation(): String?
}
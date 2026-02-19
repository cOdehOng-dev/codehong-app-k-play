package com.codehong.app.kplay.data.datasource

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeResponseDto
import com.codehong.app.kplay.data.model.performance.detail.PerformanceDetailResponseDto
import com.codehong.app.kplay.data.model.performance.list.PerformanceListResponseDto
import com.codehong.app.kplay.data.remote.KopisApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PerformanceRemoteDataSource @Inject constructor(
    private val kopisApiService: KopisApiService
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
    ): Flow<PerformanceListResponseDto> = flow {
        emit(
            kopisApiService.getPerformanceList(
                service = service,
                startDate = startDate,
                endDate = endDate,
                currentPage = currentPage,
                rowsPerPage = rowsPerPage,
                performanceState = performanceState,
                signGuCode = signGuCode,
                signGuCodeSub = signGuCodeSub,
                kidState = kidState,
                genreCode = genreCode
            )
        )
    }.flowOn(Dispatchers.IO)

    fun getPerformanceDetail(
        serviceKey: String,
        id: String,
    ): Flow<PerformanceDetailResponseDto> = flow {
        emit(kopisApiService.getPerformanceDetail(id, serviceKey))
    }.flowOn(Dispatchers.IO)

    fun getBoxOffice(
        serviceKey: String,
        startDate: String,
        endDate: String,
        catecode: String? = null,
        area: String? = null
    ): Flow<BoxOfficeResponseDto> = flow {
        emit(
            kopisApiService.getBoxOffice(serviceKey, startDate, endDate, catecode, area)
        )
    }.flowOn(Dispatchers.IO)

    fun getFestivalList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String? = null,
        signGuCodeSub: String? = null
    ): Flow<PerformanceListResponseDto> = flow {
        emit(
            kopisApiService.getFestivalList(
                serviceKey,
                startDate,
                endDate,
                currentPage,
                rowsPerPage,
                signGuCode,
                signGuCodeSub
            )
        )
    }.flowOn(Dispatchers.IO)

    fun getAwardedPerformanceList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String? = null,
        signGuCodeSub: String? = null
    ): Flow<PerformanceListResponseDto> = flow {
        emit(
            kopisApiService.getAwardedPerformanceList(
                serviceKey,
                startDate,
                endDate,
                currentPage,
                rowsPerPage,
                signGuCode,
                signGuCodeSub
            )
        )
    }.flowOn(Dispatchers.IO)

    fun searchPlace(
        serviceKey: String,
        currentPage: String,
        rowsPerPage: String,
        keyword: String
    ) = flow {
        emit(
            kopisApiService.searchPlace(
                serviceKey,
                currentPage,
                rowsPerPage,
                keyword
            )
        )
    }.flowOn(Dispatchers.IO)

    fun getPlaceDetail(
        serviceKey: String,
        id: String
    ) = flow {
        emit(
            kopisApiService.getPlaceDetail(
                id,
                serviceKey
            )
        )
    }.flowOn(Dispatchers.IO)
}
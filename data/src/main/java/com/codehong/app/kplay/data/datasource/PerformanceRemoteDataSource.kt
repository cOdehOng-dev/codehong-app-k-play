package com.codehong.app.kplay.data.datasource

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeResponse
import com.codehong.app.kplay.data.model.performance.detail.PerformanceDetailResponse
import com.codehong.app.kplay.data.model.performance.list.PerformanceListResponse
import com.codehong.app.kplay.data.remote.KopisApiService
import com.codehong.library.network.debug.TimberUtil
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
    ): Flow<PerformanceListResponse> = flow {
        TimberUtil.e("test here pass !!")
        emit(kopisApiService.getPerformanceList(
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
        ).also { TimberUtil.d("test here response = $it") })
    }.flowOn(Dispatchers.IO)

    fun getPerformanceDetail(
        serviceKey: String,
        id: String,
    ): Flow<PerformanceDetailResponse> = flow {
        emit(kopisApiService.getPerformanceDetail(id, serviceKey)).also { TimberUtil.i("test here response 11 = $it") }
    }.flowOn(Dispatchers.IO)

    fun getBoxOffice(
        serviceKey: String,
        startDate: String,
        endDate: String
    ): Flow<BoxOfficeResponse> = flow {
        emit(
            kopisApiService.getBoxOffice(serviceKey, startDate, endDate)
        ).also { TimberUtil.d("test here box office = $it") }
    }.flowOn(Dispatchers.IO)
}
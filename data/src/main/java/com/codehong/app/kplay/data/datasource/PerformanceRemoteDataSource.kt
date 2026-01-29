package com.codehong.app.kplay.data.datasource

import com.codehong.app.kplay.data.model.PerformanceListResponseDto
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
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String? = null,
        cityCode: String? = null,
        signGuCodeSub: String? = null,
        kidState: String? = null
    ): Flow<PerformanceListResponseDto> = flow {
        TimberUtil.e("test here pass !!")
        emit(kopisApiService.getPerformanceList(
            service = "3260a56d2230459f83092198140f545c", // TODO API Key 관리 필요
            startDate = startDate,
            endDate = endDate,
            currentPage = currentPage,
            rowsPerPage = rowsPerPage,
            performanceState = performanceState,
            cityCode = cityCode,
            signGuCodeSub = signGuCodeSub,
            kidState = kidState
        ).also { TimberUtil.d("test here response = $it") })
    }.flowOn(Dispatchers.IO)
}
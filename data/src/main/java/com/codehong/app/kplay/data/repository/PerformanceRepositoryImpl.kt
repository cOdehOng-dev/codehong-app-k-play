package com.codehong.app.kplay.data.repository

import com.codehong.app.kplay.data.datasource.PerformanceRemoteDataSource
import com.codehong.app.kplay.data.mapper.asDomain
import com.codehong.app.kplay.domain.model.CallStatus
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.library.network.debug.TimberUtil
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PerformanceRepositoryImpl @Inject constructor(
    private val remote: PerformanceRemoteDataSource
) : PerformanceRepository {

    override fun getPerformanceList(
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String?,
        cityCode: String?,
        signGuCodeSub: String?,
        kidState: String?
    ): Flow<CallStatus<List<PerformanceInfoItem>?>> = flow {
        remote.getPerformanceList(
            startDate,
            endDate,
            currentPage,
            rowsPerPage,
            performanceState,
            cityCode,
            signGuCodeSub,
            kidState
        ).onStart {
            emit(CallStatus.Loading)
        }.catch {
            TimberUtil.e("test here error 11 = $it")
            emit(CallStatus.Error(it))
        }.collect {
            Gson().toJson(it)?.let { json ->
                TimberUtil.d("test here response json = $json")
            }
            TimberUtil.e("test here callback = $it")
            emit(CallStatus.Success(it.performances?.map { itemDto -> itemDto.asDomain() }))
        }
    }.catch {
        TimberUtil.e("test here error 22 = $it")
        emit(CallStatus.Error(it))
    }
}
package com.codehong.app.kplay.data.repository

import com.codehong.app.kplay.data.datasource.PerformanceRemoteDataSource
import com.codehong.app.kplay.data.mapper.asDomain
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.model.place.PlaceInfoItem
import com.codehong.app.kplay.domain.repository.PerformanceRepository
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.network.CallStatus
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PerformanceRepositoryImpl @Inject constructor(
    private val remote: PerformanceRemoteDataSource
) : PerformanceRepository {

    private val gson = Gson()

    override fun getPerformanceList(
        service: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        performanceState: String?,
        signGuCode: String?,
        signGuCodeSub: String?,
        kidState: String?,
        genreCode: String?,
    ): Flow<CallStatus<List<PerformanceInfoItem>?>> = flow {
        remote.getPerformanceList(
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
        ).onStart {
            emit(CallStatus.Loading)
        }.catch {
            emit(CallStatus.Error(it))
        }.collect {
            emit(CallStatus.Success(it.performances?.map { itemDto -> itemDto.asDomain() }))
        }
    }.catch {
        emit(CallStatus.Error(it))
    }

    override fun getPerformanceDetail(
        serviceKey: String,
        id: String
    ): Flow<CallStatus<List<PerformanceDetail>?>> = flow {
        remote.getPerformanceDetail(serviceKey, id)
            .onStart {
                emit(CallStatus.Loading)
            }.catch { e ->
                emit(CallStatus.Error(e))
            }.collect {
                emit(CallStatus.Success(it.performances?.map { itemDto -> itemDto.asDomain() }))
            }
    }.catch { e ->
        emit(CallStatus.Error(e))
    }

    override fun getRankList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        genreCode: String?,
        area: String?
    ): Flow<CallStatus<List<BoxOfficeItem>?>> = flow {
        TimberUtil.i("test here getRankList genreCode = $genreCode, startDate = $startDate, endDate = $endDate, area = $area")
        remote.getBoxOffice(serviceKey, startDate, endDate, genreCode, area)
            .onStart {
                emit(CallStatus.Loading)
            }.catch { e ->
                TimberUtil.e("test here getRankList error 11 = $e")
                emit(CallStatus.Error(e))
            }.collect {
                gson.toJson(it)?.let { json ->
                    TimberUtil.d("test here getRankList json = $json")
                }
                emit(CallStatus.Success(it.boxOffices?.map { itemDto -> itemDto.asDomain() }))
            }
    }.catch { e ->
        TimberUtil.e("test here getRankList error 11 = $e")
        emit(CallStatus.Error(e))
    }

    override fun getFestivalList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String?,
        signGuCodeSub: String?
    ): Flow<CallStatus<List<PerformanceInfoItem>?>> = flow {
        remote.getFestivalList(
            serviceKey,
            startDate,
            endDate,
            currentPage,
            rowsPerPage,
            signGuCode,
            signGuCodeSub
        ).onStart {
            emit(CallStatus.Loading)
        }.catch { e ->
            emit(CallStatus.Error(e))
        }.collect {
            gson.toJson(it)?.let { json ->
                TimberUtil.d("test here getFestivalList json = $json")
            }
            emit(CallStatus.Success(it.performances?.map { itemDto -> itemDto.asDomain() }))
        }
    }.catch { e ->
        emit(CallStatus.Error(e))
    }

    override fun getAwardedPerformanceList(
        serviceKey: String,
        startDate: String,
        endDate: String,
        currentPage: String,
        rowsPerPage: String,
        signGuCode: String?,
        signGuCodeSub: String?
    ): Flow<CallStatus<List<PerformanceInfoItem>?>> = flow {
        remote.getAwardedPerformanceList(
            serviceKey,
            startDate,
            endDate,
            currentPage,
            rowsPerPage,
            signGuCode,
            signGuCodeSub
        ).onStart {
            emit(CallStatus.Loading)
        }.catch { e ->
            TimberUtil.e("test here getAwardedPerformanceList error 11 = $e")
            emit(CallStatus.Error(e))
        }.collect {
            gson.toJson(it)?.let { json ->
                TimberUtil.d("test here getAwardedPerformanceList json = $json")
            }
            emit(CallStatus.Success(it.performances?.map { itemDto -> itemDto.asDomain() }))
        }
    }.catch { e ->
        TimberUtil.e("test here getAwardedPerformanceList error 11 = $e")
        emit(CallStatus.Error(e))
    }

    override fun searchPlace(
        serviceKey: String,
        keyword: String,
        currentPage: String,
        rowsPerPage: String
    ): Flow<CallStatus<List<PlaceInfoItem>?>> = flow {
        remote.searchPlace(serviceKey, currentPage, rowsPerPage, keyword)
            .onStart {
                emit(CallStatus.Loading)
            }.catch { e ->
                emit(CallStatus.Error(e))
            }.collect {
                emit(CallStatus.Success(it.facilities?.map { item -> item.asDomain() }))
            }
    }.catch { e ->
        emit(CallStatus.Error(e))
    }

    override fun getPlaceDetail(
        serviceKey: String,
        id: String
    ): Flow<CallStatus<PlaceDetail?>> = flow {
        remote.getPlaceDetail(serviceKey, id)
            .onStart {
                emit(CallStatus.Loading)
            }.catch { e ->
                emit(CallStatus.Error(e))
            }.collect {
                emit(CallStatus.Success(it.facilities?.firstOrNull().asDomain()))
            }
    }.catch { e ->
        emit(CallStatus.Error(e))
    }
}
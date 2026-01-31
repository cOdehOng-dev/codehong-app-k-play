package com.codehong.app.kplay.ui.performance.list

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.base.BaseViewModel
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.SignGuCode.Companion.toCode
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PerformanceListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<PerformanceListEvent, PerformanceListState, PerformanceListEffect>(application) {

    companion object {
        const val EXTRA_SIGN_GU_CODE = "signGuCode"
    }

    init {
        val signGuCodeString = savedStateHandle.get<String>(EXTRA_SIGN_GU_CODE)
        val signGuCode = signGuCodeString.toCode() ?: SignGuCode.SEOUL

        val (startDate, endDate) = getDefaultDateRange()

        setState {
            copy(
                selectedSignGuCode = signGuCode,
                startDate = startDate,
                endDate = endDate
            )
        }

        callPerformanceListApi()
    }

    override fun createInitialState(): PerformanceListState = PerformanceListState()

    override fun handleEvents(event: PerformanceListEvent) {
        when (event) {
            is PerformanceListEvent.OnPerformanceClick -> {
                event.item.id?.let { id ->
                    setEffect { PerformanceListEffect.NavigateToDetail(id) }
                }
            }
            is PerformanceListEvent.OnSignGuCodeSelected -> {
                setState {
                    copy(
                        selectedSignGuCode = event.signGuCode,
                        currentPage = 1,
                        performanceList = emptyList(),
                        hasMoreData = true
                    )
                }
                callPerformanceListApi()
            }
            is PerformanceListEvent.OnDateSelected -> {
                setState {
                    copy(
                        startDate = event.startDate,
                        endDate = event.endDate,
                        currentPage = 1,
                        performanceList = emptyList(),
                        hasMoreData = true
                    )
                }
                callPerformanceListApi()
            }
            is PerformanceListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is PerformanceListEvent.OnDateChangeClick -> {
                setEffect { PerformanceListEffect.ShowDatePicker }
            }
        }
    }

    private fun getDefaultDateRange(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val calendar = Calendar.getInstance()

        val startDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.MONTH, 1)
        val endDate = dateFormat.format(calendar.time)

        return startDate to endDate
    }

    fun callPerformanceListApi() {
        val currentState = state.value

        if (currentState.isLoading) return

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                currentPage = currentState.currentPage.toString(),
                rowsPerPage = "20",
                signGuCode = currentState.selectedSignGuCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        performanceList = newList,
                        isLoading = false,
                        hasMoreData = newList.size >= 20
                    )
                }
            }
        }
    }

    private fun loadMore() {
        val currentState = state.value

        setState { copy(isLoadingMore = true, currentPage = currentState.currentPage + 1) }

        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                currentPage = (currentState.currentPage + 1).toString(),
                rowsPerPage = "20",
                signGuCode = currentState.selectedSignGuCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        performanceList = performanceList + newList,
                        isLoadingMore = false,
                        hasMoreData = newList.size >= 20
                    )
                }
            }
        }
    }
}

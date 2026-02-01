package com.codehong.app.kplay.ui.award

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.base.BaseViewModel
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AwardListViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<AwardListEvent, AwardListState, AwardListEffect>(application) {

    init {
        val (startDate, endDate) = getDefaultDateRange()

        setState {
            copy(
                startDate = startDate,
                endDate = endDate
            )
        }

        callAwardListApi()
    }

    override fun createInitialState(): AwardListState = AwardListState()

    override fun handleEvents(event: AwardListEvent) {
        when (event) {
            is AwardListEvent.OnAwardClick -> {
                event.item.id?.let { id ->
                    setEffect { AwardListEffect.NavigateToDetail(id) }
                }
            }
            is AwardListEvent.OnSignGuCodeSelected -> {
                setState {
                    copy(
                        selectedSignGuCode = event.signGuCode,
                        currentPage = 1,
                        awardList = emptyList(),
                        hasMoreData = true
                    )
                }
                callAwardListApi()
            }
            is AwardListEvent.OnDateSelected -> {
                setState {
                    copy(
                        startDate = event.startDate,
                        endDate = event.endDate,
                        currentPage = 1,
                        awardList = emptyList(),
                        hasMoreData = true
                    )
                }
                callAwardListApi()
            }
            is AwardListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is AwardListEvent.OnDateChangeClick -> {
                setEffect { AwardListEffect.ShowDatePicker }
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

    fun callAwardListApi() {
        val currentState = state.value

        if (currentState.isLoading) return

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            performanceUseCase.getAwardedPerformanceList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                currentPage = currentState.currentPage.toString(),
                rowsPerPage = "20",
                signGuCode = currentState.selectedSignGuCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        awardList = newList,
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
            performanceUseCase.getAwardedPerformanceList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                currentPage = (currentState.currentPage + 1).toString(),
                rowsPerPage = "20",
                signGuCode = currentState.selectedSignGuCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        awardList = awardList + newList,
                        isLoadingMore = false,
                        hasMoreData = newList.size >= 20
                    )
                }
            }
        }
    }
}

package com.codehong.app.kplay.ui.festival

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.type.ThemeType.Companion.toThemeType
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FestivalListViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<FestivalListEvent, FestivalListState, FestivalListEffect>(application) {

    init {
        val (startDate, endDate) = getDefaultDateRange()

        setState {
            copy(
                startDate = startDate,
                endDate = endDate
            )
        }

        callFestivalListApi()
    }

    override fun createInitialState(): FestivalListState = FestivalListState(
        themeType = performanceUseCase.getThemeType().toThemeType()
    )

    override fun handleEvents(event: FestivalListEvent) {
        when (event) {
            is FestivalListEvent.OnFestivalClick -> {
                event.item.id?.let { id ->
                    setEffect { FestivalListEffect.NavigateToDetail(id) }
                }
            }
            is FestivalListEvent.OnSignGuCodeSelected -> {
                setState {
                    copy(
                        selectedSignGuCode = event.signGuCode,
                        currentPage = 1,
                        festivalList = emptyList(),
                        hasMoreData = true
                    )
                }
                callFestivalListApi()
            }
            is FestivalListEvent.OnDateSelected -> {
                setState {
                    copy(
                        startDate = event.startDate,
                        endDate = event.endDate,
                        currentPage = 1,
                        festivalList = emptyList(),
                        hasMoreData = true
                    )
                }
                callFestivalListApi()
            }
            is FestivalListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is FestivalListEvent.OnDateChangeClick -> {
                setEffect { FestivalListEffect.ShowDatePicker }
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

    fun callFestivalListApi() {
        val currentState = state.value

        if (currentState.isLoading) return

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            performanceUseCase.getFestivalList(
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
                        festivalList = newList,
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
            performanceUseCase.getFestivalList(
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
                        festivalList = festivalList + newList,
                        isLoadingMore = false,
                        hasMoreData = newList.size >= 20
                    )
                }
            }
        }
    }
}

package com.codehong.app.kplay.ui.localtab

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.domain.type.ThemeType.Companion.toThemeType
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.kplay.util.Util
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.codehong.app.kplay.domain.type.GenreCode.Companion.toCode as toGenreCode
import com.codehong.app.kplay.domain.type.RegionCode.Companion.toCode as toSignGuCode

@HiltViewModel
class LocalTabListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<LocalTabListEvent, LocalTabListState, LocalTabListEffect>(application) {

    init {
        val genreCode = savedStateHandle.get<String>(Consts.EXTRA_GENRE_CODE).toGenreCode()
        val signGuCode = savedStateHandle.get<String>(Consts.EXTRA_SIGN_GU_CODE).toSignGuCode()

        val (startDate, endDate) = Util.getDefaultDateRange()

        setState {
            copy(
                title = genreCode?.displayName ?: "지역별 공연",
                genreCode = genreCode,
                selectedRegionCode = signGuCode,
                startDate = startDate,
                endDate = endDate
            )
        }

        callPerformanceListApi()
    }

    override fun createInitialState(): LocalTabListState = LocalTabListState(
        themeType = performanceUseCase.getThemeType().toThemeType()
    )

    override fun handleEvents(event: LocalTabListEvent) {
        when (event) {
            is LocalTabListEvent.OnPerformanceClick -> {
                event.item.id?.let { id ->
                    setEffect { LocalTabListEffect.NavigateToDetail(id) }
                }
            }
            is LocalTabListEvent.OnSignGuCodeSelected -> {
                setState {
                    copy(
                        selectedRegionCode = event.regionCode,
                        currentPage = 1,
                        performanceList = emptyList(),
                        hasMoreData = true
                    )
                }
                callPerformanceListApi()
            }
            is LocalTabListEvent.OnDateSelected -> {
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
            is LocalTabListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is LocalTabListEvent.OnDateChangeClick -> {
                setState { copy(isShowCalendar = true) }
            }
        }
    }

    fun hideCalendar() {
        setState { copy(isShowCalendar = false) }
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
                signGuCode = currentState.selectedRegionCode.code,
                genreCode = currentState.genreCode?.code
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
                signGuCode = currentState.selectedRegionCode.code,
                genreCode = currentState.genreCode?.code
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

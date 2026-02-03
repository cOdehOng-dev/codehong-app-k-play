package com.codehong.app.kplay.ui.genre.rank

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.GenreCode.Companion.toCode
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import com.codehong.library.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreRankListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<GenreRankListEvent, GenreRankListState, GenreRankListEffect>(application) {

    companion object {
        const val EXTRA_GENRE_CODE = "genreCode"
    }

    init {
        val genreCodeString = savedStateHandle.get<String>(EXTRA_GENRE_CODE)
        val genreCode = genreCodeString.toCode() ?: GenreCode.THEATER

        val (startDate, endDate) = getDefaultDateRange()

        setState {
            copy(
                initialGenreCode = genreCode,
                selectedGenreCode = genreCode,
                startDate = startDate,
                endDate = endDate
            )
        }

        callGenreRankListApi()
    }

    override fun createInitialState(): GenreRankListState = GenreRankListState()

    override fun handleEvents(event: GenreRankListEvent) {
        when (event) {
            is GenreRankListEvent.OnRankItemClick -> {
                event.item.performanceId?.let { id ->
                    setEffect { GenreRankListEffect.NavigateToDetail(id) }
                }
            }
            is GenreRankListEvent.OnGenreCodeSelected -> {
                setState {
                    copy(
                        selectedGenreCode = event.genreCode,
                        currentPage = 1,
                        genreRankList = emptyList(),
                        hasMoreData = true
                    )
                }
                callGenreRankListApi()
            }
            is GenreRankListEvent.OnDateSelected -> {
                setState {
                    copy(
                        startDate = event.startDate,
                        endDate = event.endDate,
                        currentPage = 1,
                        genreRankList = emptyList(),
                        hasMoreData = true
                    )
                }
                callGenreRankListApi()
            }
            is GenreRankListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is GenreRankListEvent.OnDateChangeClick -> {
                setEffect { GenreRankListEffect.ShowDatePicker }
            }
        }
    }

    private fun getDefaultDateRange(): Pair<String, String> {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)
        return startDate to endDate
    }

    fun callGenreRankListApi() {
        val currentState = state.value

        if (currentState.isLoading) return

        setState { copy(isLoading = true) }

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                genreCode = currentState.selectedGenreCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        genreRankList = newList,
                        isLoading = false,
                        hasMoreData = newList.size >= 10
                    )
                }
            }
        }
    }

    private fun loadMore() {
        val currentState = state.value

        setState { copy(isLoadingMore = true, currentPage = currentState.currentPage + 1) }

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                genreCode = currentState.selectedGenreCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        genreRankList = genreRankList + newList,
                        isLoadingMore = false,
                        hasMoreData = newList.size >= 10
                    )
                }
            }
        }
    }
}

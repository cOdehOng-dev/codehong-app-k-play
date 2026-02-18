package com.codehong.app.kplay.ui.genre.list

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.GenreCode.Companion.toCode
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GenreListViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<GenreListEvent, GenreListState, GenreListEffect>(application) {

    companion object {
        const val EXTRA_GENRE_CODE = "genreCode"
    }

    init {
        val genreCodeString = savedStateHandle.get<String>(EXTRA_GENRE_CODE)
        val genreCode = genreCodeString.toCode() ?: GenreCode.THEATER

        val (startDate, endDate) = getDefaultDateRange()

        setState {
            copy(
                genreCode = genreCode,
                startDate = startDate,
                endDate = endDate
            )
        }

        callGenreListApi()
    }

    override fun createInitialState(): GenreListState = GenreListState()

    override fun handleEvents(event: GenreListEvent) {
        when (event) {
            is GenreListEvent.OnPerformanceClick -> {
                event.item.id?.let { id ->
                    setEffect { GenreListEffect.NavigateToDetail(id) }
                }
            }
            is GenreListEvent.OnSignGuCodeSelected -> {
                setState {
                    copy(
                        selectedSignGuCode = event.signGuCode,
                        currentPage = 1,
                        genreList = emptyList(),
                        hasMoreData = true
                    )
                }
                callGenreListApi()
            }
            is GenreListEvent.OnDateSelected -> {
                setState {
                    copy(
                        startDate = event.startDate,
                        endDate = event.endDate,
                        currentPage = 1,
                        genreList = emptyList(),
                        hasMoreData = true
                    )
                }
                callGenreListApi()
            }
            is GenreListEvent.OnLoadMore -> {
                if (!state.value.isLoadingMore && state.value.hasMoreData) {
                    loadMore()
                }
            }
            is GenreListEvent.OnDateChangeClick -> {
                setState { copy(isShowCalendar = true) }

                // TODO HONG 필요한지 체크
//                setEffect { GenreListEffect.ShowDatePicker }
            }
        }
    }

    fun hideCalendar() {
        setState { copy(isShowCalendar = false) }
    }

    private fun getDefaultDateRange(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val calendar = Calendar.getInstance()

        val startDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.MONTH, 1)
        val endDate = dateFormat.format(calendar.time)

        return startDate to endDate
    }

    fun callGenreListApi() {
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
                signGuCode = currentState.selectedSignGuCode.code,
                genreCode = currentState.genreCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        genreList = newList,
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
                signGuCode = currentState.selectedSignGuCode.code,
                genreCode = currentState.genreCode.code
            ).collect { result ->
                val newList = result ?: emptyList()
                setState {
                    copy(
                        genreList = genreList + newList,
                        isLoadingMore = false,
                        hasMoreData = newList.size >= 20
                    )
                }
            }
        }
    }
}

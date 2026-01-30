package com.codehong.app.kplay.ui.genre.list

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode

data class GenreListState(
    val genreCode: GenreCode = GenreCode.THEATER,
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val genreList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class GenreListEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : GenreListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : GenreListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : GenreListEvent()
    data object OnLoadMore : GenreListEvent()
    data object OnDateChangeClick : GenreListEvent()
}

sealed class GenreListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : GenreListEffect()
    data object ShowDatePicker : GenreListEffect()
}

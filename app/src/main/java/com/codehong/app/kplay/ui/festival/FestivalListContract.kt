package com.codehong.app.kplay.ui.festival

import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class FestivalListState(
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val festivalList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class FestivalListEvent : ViewEvent {
    data class OnFestivalClick(val item: PerformanceInfoItem) : FestivalListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : FestivalListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : FestivalListEvent()
    data object OnLoadMore : FestivalListEvent()
    data object OnDateChangeClick : FestivalListEvent()
}

sealed class FestivalListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : FestivalListEffect()
    data object ShowDatePicker : FestivalListEffect()
}

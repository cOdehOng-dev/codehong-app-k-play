package com.codehong.app.kplay.ui.performance.list

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode

data class PerformanceListState(
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val performanceList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class PerformanceListEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : PerformanceListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : PerformanceListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : PerformanceListEvent()
    data object OnLoadMore : PerformanceListEvent()
    data object OnDateChangeClick : PerformanceListEvent()
}

sealed class PerformanceListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : PerformanceListEffect()
    data object ShowDatePicker : PerformanceListEffect()
}

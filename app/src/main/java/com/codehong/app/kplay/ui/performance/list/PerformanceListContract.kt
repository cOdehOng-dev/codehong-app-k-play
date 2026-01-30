package com.codehong.app.kplay.ui.performance.list

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.PerformanceInfoItem

data class PerformanceListState(
    val performanceList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false
) : ViewState

sealed class PerformanceListEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : PerformanceListEvent()
}

sealed class PerformanceListEffect : ViewSideEffect {
    data class NavigateToDetail(val item: PerformanceInfoItem) : PerformanceListEffect()
}
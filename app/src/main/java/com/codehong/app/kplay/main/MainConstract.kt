package com.codehong.app.kplay.main

import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class MainState(
    val performanceList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false
) : ViewState

sealed class MainEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : MainEvent()
}

sealed class MainEffect : ViewSideEffect {
    data class NavigateToDetail(val item: PerformanceInfoItem) : MainEffect()
}
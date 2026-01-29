package com.codehong.app.kplay.main

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.PerformanceInfoItem

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
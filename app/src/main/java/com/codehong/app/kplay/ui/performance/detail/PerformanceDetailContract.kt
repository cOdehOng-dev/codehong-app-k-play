package com.codehong.app.kplay.ui.performance.detail

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite

data class PerformanceDetailState(
    val performanceDetail: PerformanceDetail? = null,
    val isLoading: Boolean = false
) : ViewState

sealed class PerformanceDetailEvent : ViewEvent {
    data object OnBookingClick : PerformanceDetailEvent()
    data object OnBackClick : PerformanceDetailEvent()
}

sealed class PerformanceDetailEffect : ViewSideEffect {
    data object NavigateBack : PerformanceDetailEffect()
    data class OpenBookingPage(val relates: List<TicketingSite>) : PerformanceDetailEffect()
}

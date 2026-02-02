package com.codehong.app.kplay.ui.performance.detail

import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

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

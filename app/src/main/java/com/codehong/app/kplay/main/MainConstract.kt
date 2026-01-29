package com.codehong.app.kplay.main

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.PerformanceInfoItem

data class MainState(
    val performanceList: List<PerformanceInfoItem> = emptyList()
) : ViewState

sealed class MainEvent: ViewEvent {

}

sealed class MainEffect: ViewSideEffect {

}
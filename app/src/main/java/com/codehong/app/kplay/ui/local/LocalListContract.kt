package com.codehong.app.kplay.ui.local

import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class LocalListState(
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val performanceList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val isShowCalendar: Boolean = false
) : ViewState

sealed class LocalListEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : LocalListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : LocalListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : LocalListEvent()
    data object OnLoadMore : LocalListEvent()
    data object OnDateChangeClick : LocalListEvent()
}

sealed class LocalListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : LocalListEffect()
}

package com.codehong.app.kplay.ui.award

import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class AwardListState(
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val awardList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class AwardListEvent : ViewEvent {
    data class OnAwardClick(val item: PerformanceInfoItem) : AwardListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : AwardListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : AwardListEvent()
    data object OnLoadMore : AwardListEvent()
    data object OnDateChangeClick : AwardListEvent()
}

sealed class AwardListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : AwardListEffect()
    data object ShowDatePicker : AwardListEffect()
}
package com.codehong.app.kplay.ui.award

import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class AwardListState(
    val themeType: ThemeType = ThemeType.SYSTEM,
    val selectedRegionCode: RegionCode = RegionCode.SEOUL,
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
    data class OnSignGuCodeSelected(val regionCode: RegionCode) : AwardListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : AwardListEvent()
    data object OnLoadMore : AwardListEvent()
    data object OnDateChangeClick : AwardListEvent()
}

sealed class AwardListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : AwardListEffect()
    data object ShowDatePicker : AwardListEffect()
}
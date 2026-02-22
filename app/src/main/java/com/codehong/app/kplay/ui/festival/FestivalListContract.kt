package com.codehong.app.kplay.ui.festival

import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class FestivalListState(
    val themeType: ThemeType = ThemeType.SYSTEM,
    val selectedRegionCode: RegionCode = RegionCode.SEOUL,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val festivalList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class FestivalListEvent : ViewEvent {
    data class OnFestivalClick(val item: PerformanceInfoItem) : FestivalListEvent()
    data class OnSignGuCodeSelected(val regionCode: RegionCode) : FestivalListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : FestivalListEvent()
    data object OnLoadMore : FestivalListEvent()
    data object OnDateChangeClick : FestivalListEvent()
}

sealed class FestivalListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : FestivalListEffect()
    data object ShowDatePicker : FestivalListEffect()
}

package com.codehong.app.kplay.ui.localtab

import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class LocalTabListState(
    val title: String = "",
    val themeType: ThemeType = ThemeType.SYSTEM,
    val genreCode: GenreCode? = null,
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val startDate: String = "",
    val endDate: String = "",
    val currentPage: Int = 1,
    val performanceList: List<PerformanceInfoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val isShowCalendar: Boolean = false
) : ViewState

sealed class LocalTabListEvent : ViewEvent {
    data class OnPerformanceClick(val item: PerformanceInfoItem) : LocalTabListEvent()
    data class OnSignGuCodeSelected(val signGuCode: SignGuCode) : LocalTabListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : LocalTabListEvent()
    data object OnLoadMore : LocalTabListEvent()
    data object OnDateChangeClick : LocalTabListEvent()
}

sealed class LocalTabListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : LocalTabListEffect()
}

package com.codehong.app.kplay.ui.lounge

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode

data class LoungeState(
    val selectedTab: BottomTab = BottomTab.HOME,
    val categories: List<GenreCode> = GenreCode.entries.toList(),
    val rankList: List<BoxOfficeItem> = emptyList(),
    val selectedRankTab: RankTab = RankTab.TOP_1_10,
    val currentMonth: Int = 1,
    val myAreaList: List<PerformanceInfoItem> = emptyList(),
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val selectedGenreTab: GenreCode = GenreCode.THEATER,
    val genreRankList: List<BoxOfficeItem> = emptyList(),
    val festivalList: List<PerformanceInfoItem> = emptyList(),
) : ViewState

sealed class LoungeEvent : ViewEvent {
    data class OnTabSelected(val tab: BottomTab) : LoungeEvent()
    data class OnCategoryClick(val genreCode: GenreCode) : LoungeEvent()
    data class OnRankTabSelected(val rankTab: RankTab) : LoungeEvent()
    data class OnRankItemClick(val item: BoxOfficeItem) : LoungeEvent()
    data object OnRefreshNearbyClick : LoungeEvent()
    data class OnNearbyItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data class OnSignGuCodeUpdated(val signGuCode: SignGuCode) : LoungeEvent()
    data class OnGenreTabSelected(val genreCode: GenreCode) : LoungeEvent()
    data class OnGenreRankItemClick(val item: BoxOfficeItem) : LoungeEvent()
}

sealed class LoungeEffect : ViewSideEffect {
    data class NavigateToCategory(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToPerformanceDetail(val performanceId: String) : LoungeEffect()
    data class ShowToast(val message: String) : LoungeEffect()
    data object RequestLocationPermission : LoungeEffect()
}

enum class RankTab(
    val title: String,
    val startRank: Int,
    val endRank: Int
) {
    TOP_1_10("1~10위", 1, 10),
    TOP_11_20("11~20위", 11, 20),
    TOP_21_30("21~30위", 21, 30),
    TOP_31_40("31~40위", 31, 40),
    TOP_41_50("41~50위", 41, 50)
}

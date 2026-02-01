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
    val isMyAreaLoaded: Boolean = false,
    val selectedSignGuCode: SignGuCode = SignGuCode.SEOUL,
    val selectedGenreTab: GenreCode = GenreCode.THEATER,
    val genreRankList: List<BoxOfficeItem> = emptyList(),
    val isGenreRankLoaded: Boolean = false,
    val festivalList: List<PerformanceInfoItem> = emptyList(),
    val isFestivalLoaded: Boolean = false,
    val selectedFestivalTab: SignGuCode = SignGuCode.SEOUL,
    val festivalTabs: List<SignGuCode> = SignGuCode.entries.toList(),
    val awardedList: List<PerformanceInfoItem> = emptyList(),
    val isAwardedLoaded: Boolean = false,
    val selectedAwardedTab: SignGuCode = SignGuCode.SEOUL,
    val awardedTabs: List<SignGuCode> = SignGuCode.entries.toList(),
    val localList: List<PerformanceInfoItem> = emptyList(),
    val isLocalLoaded: Boolean = false,
    val selectedLocalTab: SignGuCode = SignGuCode.SEOUL,
    val localTabs: List<SignGuCode> = SignGuCode.entries.toList()
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
    data object OnGenreRankMoreClick : LoungeEvent()
    data class OnFestivalTabSelected(val signGuCode: SignGuCode) : LoungeEvent()
    data class OnFestivalItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnFestivalMoreClick : LoungeEvent()
    data class OnAwardedTabSelected(val signGuCode: SignGuCode) : LoungeEvent()
    data class OnAwardedItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnAwardedMoreClick : LoungeEvent()
    data class OnLocalTabSelected(val signGuCode: SignGuCode) : LoungeEvent()
    data class OnLocalItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnLocalMoreClick : LoungeEvent()
}

sealed class LoungeEffect : ViewSideEffect {
    data class NavigateToCategory(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToPerformanceDetail(val performanceId: String) : LoungeEffect()
    data class ShowToast(val message: String) : LoungeEffect()
    data object RequestLocationPermission : LoungeEffect()
    data object NavigateToFestivalList : LoungeEffect()
    data object NavigateToAwardedList : LoungeEffect()
    data class NavigateToGenreRankList(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToLocalList(val signGuCode: SignGuCode) : LoungeEffect()
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

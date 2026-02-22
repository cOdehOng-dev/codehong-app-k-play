package com.codehong.app.kplay.ui.lounge

import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.domain.model.performance.PerformanceGroup
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class LoungeState(
    val selectedTab: BottomTabType = BottomTabType.HOME,
    val themeType: ThemeType = ThemeType.SYSTEM,
    val categories: List<GenreCode> = GenreCode.entries.toList(),
    val rankList: List<BoxOfficeItem> = emptyList(),
    val selectedRankTab: RankTab = RankTab.TOP_1_10,
    val currentMonth: Int = 1,
    val myAreaList: List<PerformanceInfoItem> = emptyList(),
    val selectedRegionCode: RegionCode = RegionCode.SEOUL,
    val selectedGenreTab: GenreCode = GenreCode.THEATER,
    val genreRankList: List<BoxOfficeItem> = emptyList(),
    val festivalList: List<PerformanceInfoItem> = emptyList(),
    val selectedFestivalTab: RegionCode = RegionCode.SEOUL,
    val festivalTabList: List<RegionCode> = RegionCode.entries.toList(),
    val awardedList: List<PerformanceInfoItem> = emptyList(),
    val isAwardedLoaded: Boolean = false,
    val selectedAwardedTab: RegionCode = RegionCode.SEOUL,
    val awardedTabs: List<RegionCode> = RegionCode.entries.toList(),
    val localList: List<PerformanceInfoItem> = emptyList(),
    val selectedLocalTab: RegionCode = RegionCode.SEOUL,
    val localTabList: List<RegionCode> = RegionCode.entries.toList(),
    val apiLoading: LoungeApiLoading = LoungeApiLoading(),
    val favoriteList: List<FavoritePerformance> = emptyList(),
    val performanceGroups: List<PerformanceGroup> = emptyList(),
    val cacheSizeText: String = ""
) : ViewState

sealed class LoungeEvent : ViewEvent {
    data class OnTabSelected(val tab: BottomTabType) : LoungeEvent()
    data class OnCategoryClick(val genreCode: GenreCode) : LoungeEvent()
    data class OnRankTabSelected(val rankTab: RankTab) : LoungeEvent()
    data class OnRankItemClick(val item: BoxOfficeItem) : LoungeEvent()
    data object OnRefreshNearbyClick : LoungeEvent()
    data class OnNearbyItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data class OnSignGuCodeUpdated(val regionCode: RegionCode) : LoungeEvent()
    data class OnGenreTabSelected(val genreCode: GenreCode) : LoungeEvent()
    data class OnGenreRankItemClick(val item: BoxOfficeItem) : LoungeEvent()
    data object OnGenreRankMoreClick : LoungeEvent()
    data class OnFestivalTabSelected(val regionCode: RegionCode) : LoungeEvent()
    data class OnFestivalItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnFestivalMoreClick : LoungeEvent()
    data class OnAwardedTabSelected(val regionCode: RegionCode) : LoungeEvent()
    data class OnAwardedItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnAwardedMoreClick : LoungeEvent()
    data class OnLocalTabSelected(val regionCode: RegionCode) : LoungeEvent()
    data class OnLocalItemClick(val item: PerformanceInfoItem) : LoungeEvent()
    data object OnLocalMoreClick : LoungeEvent()
    data class OnThemeChanged(val themeType: ThemeType) : LoungeEvent()
    data class OnFavoriteItemClick(val id: String) : LoungeEvent()
    data class OnFavoriteItemDelete(val id: String) : LoungeEvent()
    data object OnCacheDeleteConfirmed : LoungeEvent()
}

sealed class LoungeEffect : ViewSideEffect {
    data class NavigateToCategory(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToPerformanceDetail(val performanceId: String) : LoungeEffect()
    data class ShowToast(val message: String) : LoungeEffect()
    data object RequestLocationPermission : LoungeEffect()
    data object RequestMyLocationTabPermission : LoungeEffect()
    data object NavigateToFestivalList : LoungeEffect()
    data object NavigateToAwardedList : LoungeEffect()
    data class NavigateToGenreRankList(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToLocalList(val regionCode: RegionCode) : LoungeEffect()
}


data class LoungeApiLoading(
    val isMonthRankLoading: Boolean = true,
    val isMyAreaLoading: Boolean = true,
    val isLocalLoading: Boolean = true,
    val isGenreRankingLoading: Boolean = true,
    val isFestivalLoading: Boolean = true,
    val isAwardLoading: Boolean = true,
    val isVenueGroupLoading: Boolean = false
)


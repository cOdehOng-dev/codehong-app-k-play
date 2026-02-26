package com.codehong.app.kplay.ui.lounge

import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.model.performance.PerformanceGroup
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class LoungeState(
    val currentTab: BottomTabType = BottomTabType.HOME,
    val themeType: ThemeType = ThemeType.SYSTEM,

    val myAreaList: List<PerformanceInfoItem> = emptyList(),
    val selectedRegionCode: RegionCode = RegionCode.SEOUL,

    val currentMonth: Int = 1,

    val rankTabList: List<RankTab> = RankTab.entries.toList(),
    val selectedRankTab: RankTab = RankTab.TOP_1_10,
    val displayRankList: List<BoxOfficeItem> = emptyList(),

    val genreTabList: List<GenreCode> = GenreCode.entries.toList(),
    val selectedGenreTab: GenreCode = GenreCode.THEATER,
    val displayGenreRankList: List<BoxOfficeItem> = emptyList(),
    val entireGenreRankList: Map<GenreCode, List<BoxOfficeItem>> = emptyMap(),

    val festivalTabList: List<RegionCode> = RegionCode.entries.toList(),
    val selectedFestivalTab: RegionCode = RegionCode.SEOUL,
    val displayFestivalList: List<PerformanceInfoItem> = emptyList(),
    val entireFestivalList: Map<RegionCode, List<PerformanceInfoItem>> = emptyMap(),

    val awardTabList: List<RegionCode> = RegionCode.entries.toList(),
    val selectedAwardTab: RegionCode = RegionCode.SEOUL,
    val displayAwardList: List<PerformanceInfoItem> = emptyList(),
    val entireAwardedList: Map<RegionCode, List<PerformanceInfoItem>> = emptyMap(),

    val localTabList: List<RegionCode> = RegionCode.entries.toList(),
    val selectedLocalTab: RegionCode = RegionCode.SEOUL,
    val displayLocalList: List<PerformanceInfoItem> = emptyList(),
    val entireLocalList: Map<RegionCode, List<PerformanceInfoItem>> = emptyMap(),


    val favoriteList: List<FavoritePerformance> = emptyList(),
    val performanceGroupList: List<PerformanceGroup> = emptyList(),


    val cacheSizeText: String = "",
    val userLat: Double? = null,
    val userLng: Double? = null,


    val apiLoading: LoungeApiLoading = LoungeApiLoading(),
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
    data class OnUserLocationObtained(val lat: Double, val lng: Double) : LoungeEvent()
}

sealed class LoungeEffect : ViewSideEffect {
    data class NavigateToCategory(val genreCode: GenreCode) : LoungeEffect()
    data class NavigateToPerformanceDetail(val performanceId: String) : LoungeEffect()
    data class ShowToast(val message: String) : LoungeEffect()
    data object RequestLocationPermission : LoungeEffect()
    data object RequestMyLocationTabPermission : LoungeEffect()
    data class NavigateToFestivalList(val regionCode: RegionCode) : LoungeEffect()
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
    val isPlaceGroupLoading: Boolean = false
)


package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.app.kplay.ui.lounge.LoungeState
import com.codehong.library.debugtool.log.TimberUtil

@Composable
fun HomeContent(
    state: LoungeState,
    onCategoryClick: (GenreCode) -> Unit,
    onRankTabSelected: (RankTab) -> Unit,
    onRankItemClick: (BoxOfficeItem) -> Unit,
    onRefreshNearbyClick: () -> Unit,
    onNearbyItemClick: (PerformanceInfoItem) -> Unit,
    onGenreTabSelected: (GenreCode) -> Unit,
    onGenreRankItemClick: (BoxOfficeItem) -> Unit,
    onGenreRankMoreClick: () -> Unit,
    onFestivalTabSelected: (RegionCode) -> Unit,
    onFestivalItemClick: (PerformanceInfoItem) -> Unit,
    onFestivalMoreClick: () -> Unit,
    onAwardedTabSelected: (RegionCode) -> Unit,
    onAwardedItemClick: (PerformanceInfoItem) -> Unit,
    onAwardedMoreClick: () -> Unit,
    onLocalTabSelected: (RegionCode) -> Unit,
    onLocalItemClick: (PerformanceInfoItem) -> Unit,
    onLocalMoreClick: () -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = listState,
    ) {
        item(key = "search_bar") {
            LoungeHeaderContent {
                TimberUtil.d("Search clicked")
            }
        }
        // TOP 배너
        item(key = "top_banner") {
            TopBannerContent(
                isLoading = state.apiLoading.isMonthRankLoading,
                bannerList = state.displayRankList.take(6),
                onBannerClick = { item ->
                    item.performanceId?.let { onRankItemClick(item) }
                }
            )
        }

        // 장르 그리드
        item(key = "genre_list") {
            GenreListContent(
                genreList = state.genreTabList,
                onClickGenre = onCategoryClick
            )
        }

        // 내 주변 공연 섹션
        item(key = "my_area") {
            MyAreaContent(
                isLoading = state.apiLoading.isMyAreaLoading,
                currentMonth = state.currentMonth,
                myAreaList = state.myAreaList.take(6),
                onClickRefresh = onRefreshNearbyClick,
                onClickProduct = onNearbyItemClick
            )
        }

        // 순위 리스트
        item(key = "rank") {
            TabRankPerformanceContent(
                title = "${state.currentMonth}월 인기 순위 Top50",
                emptyText = "랭킹 정보가 없어요",
                isLoading = state.apiLoading.isMonthRankLoading,
                tabList = state.rankTabList,
                selectedTab = state.selectedRankTab,
                rankList = state.displayRankList,
                onSelectedTab = onRankTabSelected,
                onClickProduct = onRankItemClick
            )
        }

        // 지역별 공연 섹션
        item(key = "local") {
            TabPerformanceContent(
                title = "지역별 공연이에요",
                emptyText = "지역별 공연 정보가 없어요",
                isLoading = state.apiLoading.isLocalLoading,
                tabList = state.localTabList,
                selectedTab = state.selectedLocalTab,
                performanceList = state.displayLocalList,
                onTabSelected = onLocalTabSelected,
                onClickMore = onLocalMoreClick,
                onClickProduct = onLocalItemClick
            )
        }

        // 장르별 랭킹 섹션
        item(key = "genre_rank") {
            GenreRankContent(
                isLoading = state.apiLoading.isGenreRankingLoading,
                tabList = state.genreTabList,
                selectedTab = state.selectedGenreTab,
                genreRankList = state.displayGenreRankList,
                onSelectedTab = onGenreTabSelected,
                onClickProduct = onGenreRankItemClick,
                onClickMore = onGenreRankMoreClick
            )
        }



        // 축제 섹션
        item(key = "festival") {
            TabPerformanceContent(
                title = "축제 공연은 어때요?",
                emptyText = "축제 공연 정보가 없어요",
                isLoading = state.apiLoading.isFestivalLoading,
                tabList = state.festivalTabList,
                selectedTab = state.selectedFestivalTab,
                performanceList = state.displayFestivalList,
                onTabSelected = onFestivalTabSelected,
                onClickMore = onFestivalMoreClick,
                onClickProduct = onFestivalItemClick
            )
        }

        // 수상작 섹션
        item(key = "award") {
            TabPerformanceContent(
                title = "수상작은 어때요?",
                emptyText = "수상작 정보가 없어요",
                isLoading = state.apiLoading.isAwardLoading,
                tabList = state.awardTabList,
                selectedTab = state.selectedAwardTab,
                performanceList = state.displayAwardList,
                onTabSelected = onAwardedTabSelected,
                onClickMore = onAwardedMoreClick,
                onClickProduct = onAwardedItemClick
            )
        }

        // 하단 여백
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
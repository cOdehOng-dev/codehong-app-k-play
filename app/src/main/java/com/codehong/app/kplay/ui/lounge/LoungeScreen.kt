package com.codehong.app.kplay.ui.lounge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.ui.lounge.content.home.HomeContent
import com.codehong.app.kplay.ui.lounge.content.MyLocationContent
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.liquid.tabbar.HongLiquidGlassTabBar
import com.codehong.library.widget.liquidglass.tabbar.HongLiquidGlassTabBarBuilder
import com.codehong.library.widget.liquidglass.tabbar.HongLiquidGlassTabItem
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor


@Composable
fun LoungeScreen(
    viewModel: LoungeViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoungeScreenContent(
        state = state,
        onTabSelected = { tab ->
            viewModel.setEvent(LoungeEvent.OnTabSelected(tab))
        },
        onCategoryClick = { cateCode ->
            viewModel.setEvent(LoungeEvent.OnCategoryClick(cateCode))
        },
        onRankTabSelected = { rankTab ->
            viewModel.setEvent(LoungeEvent.OnRankTabSelected(rankTab))
        },
        onRankItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnRankItemClick(item))
        },
        onRefreshNearbyClick = {
            viewModel.setEvent(LoungeEvent.OnRefreshNearbyClick)
        },
        onNearbyItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnNearbyItemClick(item))
        },
        onGenreTabSelected = { genreCode ->
            viewModel.setEvent(LoungeEvent.OnGenreTabSelected(genreCode))
        },
        onGenreRankItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnGenreRankItemClick(item))
        },
        onGenreRankMoreClick = {
            viewModel.setEvent(LoungeEvent.OnGenreRankMoreClick)
        },
        onFestivalTabSelected = { signGuCode ->
            viewModel.setEvent(LoungeEvent.OnFestivalTabSelected(signGuCode))
        },
        onFestivalItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnFestivalItemClick(item))
        },
        onFestivalMoreClick = {
            viewModel.setEvent(LoungeEvent.OnFestivalMoreClick)
        },
        onAwardedTabSelected = { signGuCode ->
            viewModel.setEvent(LoungeEvent.OnAwardedTabSelected(signGuCode))
        },
        onAwardedItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnAwardedItemClick(item))
        },
        onAwardedMoreClick = {
            viewModel.setEvent(LoungeEvent.OnAwardedMoreClick)
        },
        onLocalTabSelected = { signGuCode ->
            viewModel.setEvent(LoungeEvent.OnLocalTabSelected(signGuCode))
        },
        onLocalItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnLocalItemClick(item))
        },
        onLocalMoreClick = {
            viewModel.setEvent(LoungeEvent.OnLocalMoreClick)
        }
    )
}

@Composable
private fun LoungeScreenContent(
    state: LoungeState,
    onTabSelected: (BottomTabType) -> Unit,
    onCategoryClick: (GenreCode) -> Unit,
    onRankTabSelected: (RankTab) -> Unit,
    onRankItemClick: (BoxOfficeItem) -> Unit,
    onRefreshNearbyClick: () -> Unit,
    onNearbyItemClick: (PerformanceInfoItem) -> Unit,
    onGenreTabSelected: (GenreCode) -> Unit,
    onGenreRankItemClick: (BoxOfficeItem) -> Unit,
    onGenreRankMoreClick: () -> Unit,
    onFestivalTabSelected: (SignGuCode) -> Unit,
    onFestivalItemClick: (PerformanceInfoItem) -> Unit,
    onFestivalMoreClick: () -> Unit,
    onAwardedTabSelected: (SignGuCode) -> Unit,
    onAwardedItemClick: (PerformanceInfoItem) -> Unit,
    onAwardedMoreClick: () -> Unit,
    onLocalTabSelected: (SignGuCode) -> Unit,
    onLocalItemClick: (PerformanceInfoItem) -> Unit,
    onLocalMoreClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .hongBackground(HongColor.WHITE_100)
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = HongColor.WHITE_100.toColor(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state.selectedTab) {
                BottomTabType.HOME -> HomeContent(
                    state = state,
                    onCategoryClick = onCategoryClick,
                    onRankTabSelected = onRankTabSelected,
                    onRankItemClick = onRankItemClick,
                    onRefreshNearbyClick = onRefreshNearbyClick,
                    onNearbyItemClick = onNearbyItemClick,
                    onGenreTabSelected = onGenreTabSelected,
                    onGenreRankItemClick = onGenreRankItemClick,
                    onGenreRankMoreClick = onGenreRankMoreClick,
                    onFestivalTabSelected = onFestivalTabSelected,
                    onFestivalItemClick = onFestivalItemClick,
                    onFestivalMoreClick = onFestivalMoreClick,
                    onAwardedTabSelected = onAwardedTabSelected,
                    onAwardedItemClick = onAwardedItemClick,
                    onAwardedMoreClick = onAwardedMoreClick,
                    onLocalTabSelected = onLocalTabSelected,
                    onLocalItemClick = onLocalItemClick,
                    onLocalMoreClick = onLocalMoreClick
                )
                BottomTabType.MY_LOCATION -> MyLocationContent(
                    myAreaList = state.myAreaList,
                    selectedAreaName = state.selectedSignGuCode.displayName
                )
                BottomTabType.BOOKMARK -> BookmarkContent()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
            ) {
                HongLiquidGlassTabBar(
                    HongLiquidGlassTabBarBuilder()
                        .isDarkTheme(false)
                        .tabList(
                            listOf(
                                HongLiquidGlassTabItem(
                                    R.drawable.honglib_ic_home,
                                    BottomTabType.HOME.label
                                ),
                                HongLiquidGlassTabItem(
                                    R.drawable.honglib_ic_location,
                                    BottomTabType.MY_LOCATION.label
                                ),
                                HongLiquidGlassTabItem(
                                    R.drawable.honglib_ic_favorite,
                                    BottomTabType.BOOKMARK.label
                                )
                            )
                        )
                        .outerRadius(40)
                        .tabBarHeight(80)
                        .tabVerticalPadding(12)
                        .innerSideGap(16)
                        .onSelectedTab { i, item ->
                            when (item.label) {
                                BottomTabType.HOME.label -> onTabSelected(BottomTabType.HOME)
                                BottomTabType.MY_LOCATION.label -> onTabSelected(BottomTabType.MY_LOCATION)
                                BottomTabType.BOOKMARK.label -> onTabSelected(BottomTabType.BOOKMARK)
                            }
                        }
                        .applyOption()
                )
            }
        }
    }
}

// ===== 찜 탭 콘텐츠 (빈 화면) =====
@Composable
private fun BookmarkContent() {
    EmptyTabContent(
        title = "찜한 공연",
        description = "관심있는 공연을 찜해보세요"
    )
}

// ===== MY 탭 콘텐츠 (빈 화면) =====
@Composable
private fun MyContent() {
    EmptyTabContent(
        title = "마이페이지",
        description = "로그인하고 다양한 혜택을 받아보세요"
    )
}

// ===== 빈 탭 공통 콘텐츠 =====
@Composable
private fun EmptyTabContent(
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HongColor.DARK_GRAY_100.toColor()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = HongColor.GRAY_50.toColor(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoungeScreenPreview() {
    LoungeScreenContent(
        state = LoungeState(),
        onTabSelected = {},
        onCategoryClick = {},
        onRankTabSelected = {},
        onRankItemClick = {},
        onRefreshNearbyClick = {},
        onNearbyItemClick = {},
        onGenreTabSelected = {},
        onGenreRankItemClick = {},
        onGenreRankMoreClick = {},
        onFestivalItemClick = {},
        onFestivalTabSelected = {},
        onFestivalMoreClick = {},
        onAwardedTabSelected = {},
        onAwardedItemClick = {},
        onAwardedMoreClick = {},
        onLocalTabSelected = {},
        onLocalItemClick = {},
        onLocalMoreClick = {}
    )
}
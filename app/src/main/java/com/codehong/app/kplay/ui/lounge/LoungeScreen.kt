package com.codehong.app.kplay.ui.lounge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.ui.BottomTab
import com.codehong.app.kplay.ui.lounge.content.GenreListContent
import com.codehong.app.kplay.ui.lounge.content.GenreRankContent
import com.codehong.app.kplay.ui.lounge.content.LoungeHeaderContent
import com.codehong.app.kplay.ui.lounge.content.MyAreaContent
import com.codehong.app.kplay.ui.lounge.content.TabPerformanceContent
import com.codehong.app.kplay.ui.lounge.content.TabRankPerformanceContent
import com.codehong.app.kplay.ui.lounge.content.TopBannerContent
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongScaleType
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
    onTabSelected: (BottomTab) -> Unit,
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
        topBar = {
            LoungeHeaderContent {
                TimberUtil.d("Search clicked")
            }
        },
        bottomBar = {
            LoungeBottomNavigation(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state.selectedTab) {
                BottomTab.HOME -> HomeContent(
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
                BottomTab.SEARCH -> SearchContent()
                BottomTab.BOOKMARK -> BookmarkContent()
                BottomTab.MY -> MyContent()
            }
        }
    }
}



@Composable
private fun LoungeBottomNavigation(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .shadow(elevation = 8.dp)
            .height(64.dp),
        containerColor = HongColor.WHITE_100.toColor(),
        tonalElevation = 0.dp
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val iconColor = if (isSelected) HongColor.MAIN_ORANGE_100.toColor() else HongColor.GRAY_50.toColor()

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    TimberUtil.d("Bottom tab clicked: ${tab.title}")
                    onTabSelected(tab)
                },
                icon = {
                    TabIcon(
                        modifier = Modifier.size(24.dp),
                        tab = tab,
                        isSelected = isSelected,
                        color = iconColor
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HongColor.MAIN_ORANGE_100.toColor(),
                    selectedTextColor = HongColor.MAIN_ORANGE_100.toColor(),
                    unselectedIconColor = HongColor.GRAY_50.toColor(),
                    unselectedTextColor = HongColor.GRAY_50.toColor(),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun TabIcon(
    tab: BottomTab,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    when (tab) {
        BottomTab.HOME -> HomeIcon(color = color, filled = isSelected, modifier = modifier)
        BottomTab.SEARCH -> SearchIcon(color = color, filled = isSelected, modifier = modifier)
        BottomTab.BOOKMARK -> BookmarkIcon(color = color, filled = isSelected, modifier = modifier)
        BottomTab.MY -> PersonIcon(color = color, filled = isSelected, modifier = modifier)
    }
}


@Composable
private fun HomeIcon(
    color: Color,
    filled: Boolean,
    modifier: Modifier = Modifier
) {
    HongImageCompose(
        HongImageBuilder()
            .width(27)
            .height(27)
            .imageInfo(R.drawable.honglib_ic_home)
            .imageColor(if (filled) HongColor.MAIN_ORANGE_100 else HongColor.GRAY_50)
            .scaleType(HongScaleType.CENTER_CROP)
            .applyOption()
    )
}

@Composable
private fun BookmarkIcon(
    color: Color,
    filled: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.08f
        val heartPath = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.85f)
            cubicTo(
                size.width * 0.1f, size.height * 0.55f,
                size.width * 0.1f, size.height * 0.2f,
                size.width * 0.5f, size.height * 0.35f
            )
            cubicTo(
                size.width * 0.9f, size.height * 0.2f,
                size.width * 0.9f, size.height * 0.55f,
                size.width * 0.5f, size.height * 0.85f
            )
            close()
        }

        if (filled) {
            drawPath(heartPath, color)
        } else {
            drawPath(heartPath, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
        }
    }
}

@Composable
private fun SearchIcon(
    color: Color,
    filled: Boolean,
    modifier: Modifier = Modifier
) {
    HongImageCompose(
        HongImageBuilder()
            .width(27)
            .height(27)
            .imageInfo(R.drawable.honglib_ic_search)
            .imageColor(if (filled) HongColor.MAIN_ORANGE_100 else HongColor.GRAY_50)
            .scaleType(HongScaleType.CENTER_CROP)
            .applyOption()
    )
}

@Composable
private fun PersonIcon(
    color: Color,
    filled: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.08f

        if (filled) {
            drawCircle(
                color = color,
                radius = size.width * 0.22f,
                center = Offset(size.width * 0.5f, size.height * 0.28f)
            )
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(size.width * 0.15f, size.height * 0.5f),
                size = Size(size.width * 0.7f, size.height * 0.55f)
            )
        } else {
            drawCircle(
                color = color,
                radius = size.width * 0.22f,
                center = Offset(size.width * 0.5f, size.height * 0.28f),
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.15f, size.height * 0.5f),
                size = Size(size.width * 0.7f, size.height * 0.55f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

// ===== 홈 탭 콘텐츠 =====

@Composable
private fun HomeContent(
    state: LoungeState,
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
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        // TOP 배너
        item(key = "top_banner") {
            TopBannerContent(
                isLoading = state.apiLoading.isMonthRankLoading,
                bannerList = state.rankList.take(6),
                onBannerClick = { item ->
                    item.performanceId?.let { onRankItemClick(item) }
                }
            )
        }

        // 카테고리 그리드
        item(key = "genre_list") {
            GenreListContent(
                genreList = state.categories,
                onClickGenre = onCategoryClick
            )
        }

        // 내 주변 공연 섹션
        item(key = "my_area") {
            MyAreaContent(
                isLoading = state.apiLoading.isMyAreaLoading,
                currentMonth = state.currentMonth,
                myAreaList = state.myAreaList,
                onClickRefresh = onRefreshNearbyClick,
                onClickProduct = onNearbyItemClick
            )
        }

        // 순위 리스트
        item(key = "rank_list") {
            TabRankPerformanceContent(
                title = "${state.currentMonth}월 인기 순위 Top50",
                emptyText = "랭킹 정보가 없어요",
                isLoading = state.apiLoading.isMonthRankLoading,
                selectedTab = state.selectedRankTab,
                tabList = state.rankList,
                onSelectedTab = onRankTabSelected,
                onClickProduct = onRankItemClick
            )
        }

        // 지역별 공연 섹션
        item(key = "local_content") {
            TabPerformanceContent(
                title = "지역별 공연이에요",
                emptyText = "지역별 공연 정보가 없어요",
                isLoading = state.apiLoading.isLocalLoading,
                tabList = state.localTabList,
                selectedTab = state.selectedLocalTab,
                performanceList = state.localList,
                onTabSelected = onLocalTabSelected,
                onClickMore = onLocalMoreClick,
                onClickProduct = onLocalItemClick
            )
        }

        // 장르별 랭킹 섹션
        item(key = "genre_rank") {
            GenreRankContent(
                isLoading = state.apiLoading.isGenreRankingLoading,
                genreList = state.categories,
                selectedTab = state.selectedGenreTab,
                genreRankList = state.genreRankList,
                onSelectedTab = onGenreTabSelected,
                onClickProduct = onGenreRankItemClick,
                onClickMore = onGenreRankMoreClick
            )
        }



        // 축제 섹션
        item(key = "festival_section") {
            TabPerformanceContent(
                title = "축제 공연은 어때요?",
                emptyText = "축제 공연 정보가 없어요",
                isLoading = state.apiLoading.isFestivalLoading,
                tabList = state.festivalTabList,
                selectedTab = state.selectedFestivalTab,
                performanceList = state.festivalList,
                onTabSelected = onFestivalTabSelected,
                onClickMore = onFestivalMoreClick,
                onClickProduct = onFestivalItemClick
            )
        }

        // 수상작 섹션
        item(key = "awarded_section") {
            TabPerformanceContent(
                title = "수상작은 어때요?",
                emptyText = "수상작 정보가 없어요",
                isLoading = state.apiLoading.isAwardLoading,
                tabList = state.awardedTabs,
                selectedTab = state.selectedAwardedTab,
                performanceList = state.awardedList,
                onTabSelected = onAwardedTabSelected,
                onClickMore = onAwardedMoreClick,
                onClickProduct = onAwardedItemClick
            )
        }

        // 하단 여백
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ===== 검색 탭 콘텐츠 (빈 화면) =====
@Composable
private fun SearchContent() {
    EmptyTabContent(
        title = "검색",
        description = "공연, 장소, 아티스트를 검색해보세요"
    )
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



@Preview(showBackground = true)
@Composable
private fun LoungeBottomNavigationPreview() {
    LoungeBottomNavigation(
        selectedTab = BottomTab.HOME,
        onTabSelected = {}
    )
}

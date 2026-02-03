package com.codehong.app.kplay.ui.lounge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.ui.BottomTab
import com.codehong.app.kplay.ui.lounge.screen.ArrowRightIcon
import com.codehong.app.kplay.ui.lounge.screen.LoungeHeaderContent
import com.codehong.app.kplay.ui.lounge.screen.RankTab
import com.codehong.app.kplay.ui.lounge.screen.TopBannerSection
import com.codehong.library.network.debug.TimberUtil
import com.codehong.library.widget.R
import com.codehong.library.widget.image.HongImageBuilder
import com.codehong.library.widget.image.HongImageCompose
import kotlinx.coroutines.launch

private val ColorPrimary = Color(0xFF2DB400) //Color(0xFF2AC1BC)
private val BackgroundColor = Color.White
private val Gray = Color(0xFF999999)
private val DarkGray = Color(0xFF333333)

// 순위 메달 컬러
private val GoldColor = Color(0xFFFFD700)
private val SilverColor = Color(0xFFC0C0C0)
private val BronzeColor = Color(0xFFCD7F32)

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
        containerColor = BackgroundColor,
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
        containerColor = BackgroundColor,
        tonalElevation = 0.dp
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val iconColor = if (isSelected) ColorPrimary else Gray

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    TimberUtil.d("Bottom tab clicked: ${tab.title}")
                    onTabSelected(tab)
                },
                icon = {
                    TabIcon(
                        tab = tab,
                        isSelected = isSelected,
                        color = iconColor,
                        modifier = Modifier.size(24.dp)
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
                    selectedIconColor = ColorPrimary,
                    selectedTextColor = ColorPrimary,
                    unselectedIconColor = Gray,
                    unselectedTextColor = Gray,
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
        BottomTab.SEARCH -> PersonIcon(color = color, filled = isSelected, modifier = modifier)
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
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.08f

        val roofPath = Path().apply {
            moveTo(size.width * 0.1f, size.height * 0.45f)
            lineTo(size.width * 0.5f, size.height * 0.12f)
            lineTo(size.width * 0.9f, size.height * 0.45f)
        }

        if (filled) {
            val housePath = Path().apply {
                moveTo(size.width * 0.5f, size.height * 0.12f)
                lineTo(size.width * 0.9f, size.height * 0.45f)
                lineTo(size.width * 0.9f, size.height * 0.9f)
                lineTo(size.width * 0.1f, size.height * 0.9f)
                lineTo(size.width * 0.1f, size.height * 0.45f)
                close()
            }
            drawPath(housePath, color)
        } else {
            drawPath(roofPath, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
            drawRoundRect(
                color = color,
                topLeft = Offset(size.width * 0.18f, size.height * 0.45f),
                size = Size(size.width * 0.64f, size.height * 0.45f),
                cornerRadius = CornerRadius(size.width * 0.05f),
                style = Stroke(width = strokeWidth)
            )
        }
    }
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
        item {
            TopBannerSection(
                isLoading = state.loading.isBannerLoading,
                bannerList = state.rankList.take(6),
                onBannerClick = { item ->
                    item.performanceId?.let { onRankItemClick(item) }
                }
            )
        }

        // 카테고리 그리드
        item {
            CategoryGridNonLazy(
                categories = state.categories,
                onCategoryClick = onCategoryClick
            )
        }

        // 내 주변 공연 섹션
        item {
            MyAreaSection(
                currentMonth = state.currentMonth,
                signGuCode = state.selectedSignGuCode,
                myAreaList = state.myAreaList,
                isLoaded = state.isMyAreaLoaded,
                onRefreshClick = onRefreshNearbyClick,
                onItemClick = onNearbyItemClick
            )
        }

        // 내 주변 공연 전체보기 버튼
        item {
            ViewAllMyAreaPerformancesButton(
                onClick = {
                    // TODO: 내 주변 공연 전체보기 페이지 연결
                    TimberUtil.d("View All My Area Performances button clicked")
                }
            )
        }

        // 지역별 공연 섹션
        item {
            LocalSection(
                localTabs = state.localTabs,
                selectedLocalTab = state.selectedLocalTab,
                localList = state.localList,
                isLoaded = state.isLocalLoaded,
                onLocalTabSelected = onLocalTabSelected,
                onItemClick = onLocalItemClick,
                onMoreClick = onLocalMoreClick
            )
        }

        // 장르별 랭킹 섹션
        item {
            GenreRankSection(
                categories = state.categories,
                selectedGenreTab = state.selectedGenreTab,
                genreRankList = state.genreRankList,
                isLoaded = state.isGenreRankLoaded,
                onGenreTabSelected = onGenreTabSelected,
                onItemClick = onGenreRankItemClick,
                onMoreClick = onGenreRankMoreClick
            )
        }

        // 인기 순위 제목
        item {
            Text(
                text = "${state.currentMonth}월 인기 순위 Top50",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // 순위 탭
        item {
            RankTabRow(
                selectedTab = state.selectedRankTab,
                onTabSelected = onRankTabSelected
            )
        }

        // 순위 리스트 (가로 스크롤)
        item {
            val filteredRankList = state.rankList.filter { item ->
                val rank = item.rank?.toIntOrNull() ?: 0
                rank in state.selectedRankTab.startRank..state.selectedRankTab.endRank
            }

            // 탭 변경 시 스크롤 위치 초기화
            val rankScrollState = rememberScrollState()
            LaunchedEffect(state.selectedRankTab) {
                rankScrollState.scrollTo(0)
            }

            if (filteredRankList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "순위 정보를 불러오는 중...",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rankScrollState)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    filteredRankList.forEach { item ->
                        HorizontalRankItem(
                            item = item,
                            onClick = { onRankItemClick(item) }
                        )
                    }
                }
            }
        }

        // 축제 섹션
        item {
            FestivalSection(
                festivalTabs = state.festivalTabs,
                selectedFestivalTab = state.selectedFestivalTab,
                festivalList = state.festivalList,
                isLoaded = state.isFestivalLoaded,
                onFestivalTabSelected = onFestivalTabSelected,
                onItemClick = onFestivalItemClick,
                onMoreClick = onFestivalMoreClick
            )
        }

        // 수상작 섹션
        item {
            AwardedSection(
                awardedTabs = state.awardedTabs,
                selectedAwardedTab = state.selectedAwardedTab,
                awardedList = state.awardedList,
                isLoaded = state.isAwardedLoaded,
                onAwardedTabSelected = onAwardedTabSelected,
                onItemClick = onAwardedItemClick,
                onMoreClick = onAwardedMoreClick
            )
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CategoryGridNonLazy(
    categories: List<GenreCode>,
    onCategoryClick: (GenreCode) -> Unit
) {
    val rows = categories.chunked(5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rowItems.forEach { cateCode ->
                    Box(modifier = Modifier.weight(1f)) {
                        CategoryItem(
                            genreCode = cateCode,
                            onClick = { onCategoryClick(cateCode) }
                        )
                    }
                }
                // 빈 공간 채우기
                repeat(5 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CategoryItem(
    genreCode: GenreCode,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val iconResId = remember(genreCode) {
        context.resources.getIdentifier(
            genreCode.iconResName,
            "drawable",
            context.packageName
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 40.dp),
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        if (iconResId != 0) {
            HongImageCompose(
                HongImageBuilder()
                    .width(50)
                    .height(50)
                    .imageInfo(iconResId)
                    .applyOption()
            )
        }

        Text(
            text = genreCode.displayName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = DarkGray,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        )
    }
}

// ===== 내 주변 공연 섹션 =====
@Composable
private fun MyAreaSection(
    currentMonth: Int,
    signGuCode: SignGuCode,
    myAreaList: List<PerformanceInfoItem>,
    isLoaded: Boolean,
    onRefreshClick: () -> Unit,
    onItemClick: (PerformanceInfoItem) -> Unit
) {
    val rotationAngle = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 제목 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // signGuCode.displayName
            Text(
                text = "${currentMonth}월 내 주변 공연",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 1.dp)
                    .graphicsLayer {
                        rotationZ = rotationAngle.value
                    }
                    .clickable(onClick = {
                        onRefreshClick()
                        coroutineScope.launch {
                            rotationAngle.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(durationMillis = 1000)
                            )
                            rotationAngle.snapTo(0f)
                        }
                    }),
                contentAlignment = Alignment.Center
            ) {
                HongImageCompose(
                    HongImageBuilder()
                        .width(18)
                        .height(18)
                        .imageInfo(R.drawable.honglib_ic_refresh)
                        .applyOption()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 가로 스크롤 리스트
        if (myAreaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoaded) "주변 공연 정보가 없습니다" else "주변 공연 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                myAreaList.forEach { item ->
                    MyAreaItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyAreaItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(130.dp, 170.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.genre.isNullOrBlank()) {
                SmallBadge(text = item.genre)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.name ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        val period = buildString {
            item.startDate?.let { append(it) }
            if (!item.startDate.isNullOrBlank() && !item.endDate.isNullOrBlank()) {
                append(" ~ ")
            }
            item.endDate?.let { append(it) }
        }
        if (period.isNotBlank()) {
            Text(
                text = period,
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ===== 지역별 공연 섹션 =====
@Composable
private fun LocalSection(
    localTabs: List<SignGuCode>,
    selectedLocalTab: SignGuCode,
    localList: List<PerformanceInfoItem>,
    isLoaded: Boolean,
    onLocalTabSelected: (SignGuCode) -> Unit,
    onItemClick: (PerformanceInfoItem) -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 제목 + 더보기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지역별 공연이에요",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )

            ArrowRightIcon(onMoreClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 지역 탭 (TabLayout)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = localTabs,
                key = { _, item -> "local_${item.code}" }
            ) { _, signGuCode ->
                LocalTabChip(
                    text = signGuCode.displayName,
                    isSelected = signGuCode == selectedLocalTab,
                    onClick = { onLocalTabSelected(signGuCode) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 변경 시 스크롤 위치 초기화
        val localScrollState = rememberScrollState()
        LaunchedEffect(selectedLocalTab) {
            localScrollState.scrollTo(0)
        }

        // 지역별 공연 리스트
        if (localList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoaded) "해당 지역의 공연 정보가 없습니다" else "지역별 공연 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .horizontalScroll(localScrollState)
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                localList.forEach { item ->
                    LocalItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LocalTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) ColorPrimary else BackgroundColor
            )
            .border(
                width = 1.dp,
                color = ColorPrimary,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else ColorPrimary
        )
    }
}

@Composable
private fun LocalItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(130.dp, 170.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.genre.isNullOrBlank()) {
                SmallBadge(text = item.genre)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.name ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        val period = buildString {
            item.startDate?.let { append(it) }
            if (!item.startDate.isNullOrBlank() && !item.endDate.isNullOrBlank()) {
                append(" ~ ")
            }
            item.endDate?.let { append(it) }
        }
        if (period.isNotBlank()) {
            Text(
                text = period,
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ===== 장르별 랭킹 섹션 =====
@Composable
private fun GenreRankSection(
    categories: List<GenreCode>,
    selectedGenreTab: GenreCode,
    genreRankList: List<BoxOfficeItem>,
    isLoaded: Boolean,
    onGenreTabSelected: (GenreCode) -> Unit,
    onItemClick: (BoxOfficeItem) -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 제목 + 더보기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "장르별 랭킹이에요",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )

            ArrowRightIcon(onMoreClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 장르 탭 (TabLayout)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = categories,
                key = { _, item -> item.code }
            ) { _, genreCode ->
                GenreTabChip(
                    text = genreCode.displayName,
                    isSelected = genreCode == selectedGenreTab,
                    onClick = { onGenreTabSelected(genreCode) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 변경 시 스크롤 위치 초기화
        val genreScrollState = rememberScrollState()
        LaunchedEffect(selectedGenreTab) {
            genreScrollState.scrollTo(0)
        }

        // 장르별 랭킹 가로 리스트
        if (genreRankList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoaded) "해당 장르의 랭킹 정보가 없습니다" else "랭킹 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .horizontalScroll(genreScrollState)
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                genreRankList.take(10).forEachIndexed { index, item ->
                    GenreRankItem(
                        item = item,
                        rank = index + 1,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

// ===== 축제 섹션 =====
@Composable
private fun FestivalSection(
    festivalTabs: List<SignGuCode>,
    selectedFestivalTab: SignGuCode,
    festivalList: List<PerformanceInfoItem>,
    isLoaded: Boolean,
    onFestivalTabSelected: (SignGuCode) -> Unit,
    onItemClick: (PerformanceInfoItem) -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 제목 + 더보기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지역 축제도 많아요",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )

            ArrowRightIcon(onMoreClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 지역 탭 (TabLayout)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = festivalTabs,
                key = { _, item -> item.code }
            ) { _, signGuCode ->
                FestivalTabChip(
                    text = signGuCode.displayName,
                    isSelected = signGuCode == selectedFestivalTab,
                    onClick = { onFestivalTabSelected(signGuCode) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 변경 시 스크롤 위치 초기화
        val festivalScrollState = rememberScrollState()
        LaunchedEffect(selectedFestivalTab) {
            festivalScrollState.scrollTo(0)
        }

        // 축제 리스트
        if (festivalList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoaded) "해당 지역의 축제 정보가 없습니다" else "축제 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .horizontalScroll(festivalScrollState)
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                festivalList.forEach { item ->
                    FestivalItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FestivalTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) ColorPrimary else BackgroundColor
            )
            .border(
                width = 1.dp,
                color = ColorPrimary,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else ColorPrimary
        )
    }
}

@Composable
private fun FestivalItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(130.dp, 170.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.genre.isNullOrBlank()) {
                SmallBadge(text = item.genre)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.name ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        val period = buildString {
            item.startDate?.let { append(it) }
            if (!item.startDate.isNullOrBlank() && !item.endDate.isNullOrBlank()) {
                append(" ~ ")
            }
            item.endDate?.let { append(it) }
        }
        if (period.isNotBlank()) {
            Text(
                text = period,
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ===== 수상작 섹션 =====
@Composable
private fun AwardedSection(
    awardedTabs: List<SignGuCode>,
    selectedAwardedTab: SignGuCode,
    awardedList: List<PerformanceInfoItem>,
    isLoaded: Boolean,
    onAwardedTabSelected: (SignGuCode) -> Unit,
    onItemClick: (PerformanceInfoItem) -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // 제목 + 더보기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "수상작은 어때요?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )

            ArrowRightIcon(onMoreClick)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 지역 탭 (TabLayout)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = awardedTabs,
                key = { _, item -> "awarded_${item.code}" }
            ) { _, signGuCode ->
                AwardedTabChip(
                    text = signGuCode.displayName,
                    isSelected = signGuCode == selectedAwardedTab,
                    onClick = { onAwardedTabSelected(signGuCode) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 변경 시 스크롤 위치 초기화
        val awardedScrollState = rememberScrollState()
        LaunchedEffect(selectedAwardedTab) {
            awardedScrollState.scrollTo(0)
        }

        // 수상작 리스트
        if (awardedList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoaded) "해당 지역의 수상작 정보가 없습니다" else "수상작 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = Gray
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .horizontalScroll(awardedScrollState)
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                awardedList.forEach { item ->
                    AwardedItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AwardedTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) ColorPrimary else BackgroundColor
            )
            .border(
                width = 1.dp,
                color = ColorPrimary,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else ColorPrimary
        )
    }
}

@Composable
private fun AwardedItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(130.dp, 170.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.genre.isNullOrBlank()) {
                SmallBadge(text = item.genre)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.name ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        val period = buildString {
            item.startDate?.let { append(it) }
            if (!item.startDate.isNullOrBlank() && !item.endDate.isNullOrBlank()) {
                append(" ~ ")
            }
            item.endDate?.let { append(it) }
        }
        if (period.isNotBlank()) {
            Text(
                text = period,
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 수상 정보
        if (!item.awards.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.awards ?: "",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Gray,
                lineHeight = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun GenreTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) ColorPrimary else BackgroundColor
            )
            .border(
                width = 1.dp,
                color = ColorPrimary,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else ColorPrimary
        )
    }
}

@Composable
private fun GenreRankItem(
    item: BoxOfficeItem,
    rank: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지 + 순위 배지
        Box {
            AsyncImage(
                model = item.posterUrl,
                contentDescription = item.performanceName,
                modifier = Modifier
                    .size(130.dp, 170.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 순위 배지 (좌측 하단)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (rank) {
                            1 -> GoldColor
                            2 -> SilverColor
                            3 -> BronzeColor
                            else -> DarkGray.copy(alpha = 0.8f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.category.isNullOrBlank()) {
                SmallBadge(text = item.category)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.performanceName ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        if (!item.performancePeriod.isNullOrBlank()) {
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HorizontalRankItem(
    item: BoxOfficeItem,
    onClick: () -> Unit
) {
    val rank = item.rank?.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        // 포스터 이미지 + 순위 배지
        Box {
            AsyncImage(
                model = item.posterUrl,
                contentDescription = item.performanceName,
                modifier = Modifier
                    .size(130.dp, 170.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 순위 배지 (좌측 하단)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when (rank) {
                            1 -> GoldColor
                            2 -> SilverColor
                            3 -> BronzeColor
                            else -> DarkGray.copy(alpha = 0.8f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 뱃지 (장르, 지역)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!item.category.isNullOrBlank()) {
                SmallBadge(text = item.category)
            }
            if (!item.area.isNullOrBlank()) {
                SmallBadge(text = item.area)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 공연명
        Text(
            text = item.performanceName ?: "",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        if (!item.performancePeriod.isNullOrBlank()) {
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 10.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RefreshIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.1f
        val radius = size.width * 0.35f
        val center = Offset(size.width * 0.5f, size.height * 0.5f)

        // 원형 화살표 (3/4 호)
        drawArc(
            color = color,
            startAngle = -60f,
            sweepAngle = 280f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 화살표 머리
        val arrowPath = Path().apply {
            val arrowX = center.x + radius * kotlin.math.cos(Math.toRadians(-60.0)).toFloat()
            val arrowY = center.y + radius * kotlin.math.sin(Math.toRadians(-60.0)).toFloat()

            moveTo(arrowX - size.width * 0.12f, arrowY - size.width * 0.08f)
            lineTo(arrowX, arrowY)
            lineTo(arrowX + size.width * 0.08f, arrowY - size.width * 0.12f)
        }
        drawPath(arrowPath, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
    }
}

@Composable
private fun ViewAllMyAreaPerformancesButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(48.dp) // Fixed height for consistency
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, ColorPrimary, RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "내 주변 공연 전체보기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrimary
        )
        Spacer(modifier = Modifier.width(4.dp))
        // Placeholder for arrow icon, ideally replace with an actual icon resource
        Text(
            text = ">", // Using text for now, can be replaced with an actual icon
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrimary
        )
    }
}

// ===== 순위 탭 (타원형 Pill 스타일 + 스크롤) =====
@Composable
private fun RankTabRow(
    selectedTab: RankTab,
    onTabSelected: (RankTab) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(RankTab.entries.size) { index ->
            val tab = RankTab.entries[index]
            val isSelected = tab == selectedTab
            RankTabChip(
                text = tab.title,
                isSelected = isSelected,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun RankTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) ColorPrimary else BackgroundColor
            )
            .border(
                width = 1.dp,
                color = ColorPrimary,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else ColorPrimary
        )
    }
}

// ===== 순위 리스트 아이템 =====
@Composable
private fun RankListItem(
    item: BoxOfficeItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 순위 텍스트
        val rank = item.rank?.toIntOrNull() ?: 0
        val rankColor = when (rank) {
            1 -> GoldColor
            2 -> SilverColor
            3 -> BronzeColor
            else -> DarkGray
        }

        Text(
            text = "${rank}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = rankColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.performanceName,
            modifier = Modifier
                .size(100.dp, 130.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 정보 영역
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 장르, 지역 뱃지 (분리)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!item.category.isNullOrBlank()) {
                    SmallBadge(text = item.category)
                }
                if (!item.area.isNullOrBlank()) {
                    SmallBadge(text = item.area)
                }
            }

            // 공연명
            Text(
                text = item.performanceName ?: "",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 장소명
            Text(
                text = item.placeName ?: "",
                fontSize = 13.sp,
                color = Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 공연 기간
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 12.sp,
                color = ColorPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SmallBadge(text: String?) {
    if (text.isNullOrEmpty()) return
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = Gray
        )
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
                color = DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Gray,
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

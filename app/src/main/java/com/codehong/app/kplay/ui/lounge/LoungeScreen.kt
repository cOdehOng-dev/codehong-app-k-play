package com.codehong.app.kplay.ui.lounge

import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codehong.app.kplay.R
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.widget.image.HongImageBuilder
import com.codehong.library.widget.image.HongImageCompose
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LoungeScreen"

// 배민 스타일 컬러
private val BaeminPrimary = Color(0xFF2DB400) //Color(0xFF2AC1BC)
private val BaeminBackground = Color.White
private val BaeminGray = Color(0xFF999999)
private val BaeminDarkGray = Color(0xFF333333)

// 순위 메달 컬러
private val GoldColor = Color(0xFFFFD700)
private val SilverColor = Color(0xFFC0C0C0)
private val BronzeColor = Color(0xFFCD7F32)

@Composable
fun LoungeScreen(
    viewModel: LoungeViewModel = hiltViewModel()
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
        onFestivalTabSelected = { signGuCode ->
            viewModel.setEvent(LoungeEvent.OnFestivalTabSelected(signGuCode))
        },
        onFestivalItemClick = { item ->
            viewModel.setEvent(LoungeEvent.OnFestivalItemClick(item))
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
    onFestivalTabSelected: (SignGuCode) -> Unit,
    onFestivalItemClick: (PerformanceInfoItem) -> Unit
) {
    Scaffold(
        containerColor = BaeminBackground,
        topBar = {
            LoungeHeader(
                onSearchClick = {
                    Log.d(TAG, "Search clicked")
                },
                onNotificationClick = {
                    Log.d(TAG, "Notification icon clicked")
                }
            )
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
                    onFestivalTabSelected = onFestivalTabSelected,
                    onFestivalItemClick = onFestivalItemClick
                )
                BottomTab.SEARCH -> SearchContent()
                BottomTab.BOOKMARK -> BookmarkContent()
                BottomTab.MY -> MyContent()
            }
        }
    }
}

// ===== Header (배민 스타일) =====
@Composable
private fun LoungeHeader(
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(BaeminBackground)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 검색바 (헤더 내)
        Row(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .clickable(onClick = onSearchClick)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchIcon(color = BaeminPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "찾고 싶은 공연, 배우를 검색해보세요",
                fontSize = 14.sp,
                color = BaeminGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 알림 아이콘
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onNotificationClick),
            contentAlignment = Alignment.Center
        ) {
            NotificationIcon(
                color = BaeminDarkGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ===== Bottom Navigation (배민 스타일) =====
@Composable
private fun LoungeBottomNavigation(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .shadow(elevation = 8.dp)
            .height(64.dp),
        containerColor = BaeminBackground,
        tonalElevation = 0.dp
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val iconColor = if (isSelected) BaeminPrimary else BaeminGray

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    Log.d(TAG, "Bottom tab clicked: ${tab.title}")
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
                    selectedIconColor = BaeminPrimary,
                    selectedTextColor = BaeminPrimary,
                    unselectedIconColor = BaeminGray,
                    unselectedTextColor = BaeminGray,
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
        BottomTab.SEARCH -> SearchIcon(color = color, modifier = modifier)
        BottomTab.BOOKMARK -> BookmarkIcon(color = color, filled = isSelected, modifier = modifier)
        BottomTab.MY -> PersonIcon(color = color, filled = isSelected, modifier = modifier)
    }
}

// ===== Custom Icons =====
@Composable
private fun SearchIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.1f
        val radius = size.width * 0.32f
        val center = Offset(size.width * 0.4f, size.height * 0.4f)

        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawLine(
            color = color,
            start = Offset(center.x + radius * 0.7f, center.y + radius * 0.7f),
            end = Offset(size.width * 0.85f, size.height * 0.85f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ArrowRightIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.12f

        // > 모양 화살표
        val arrowPath = Path().apply {
            moveTo(size.width * 0.35f, size.height * 0.2f)
            lineTo(size.width * 0.65f, size.height * 0.5f)
            lineTo(size.width * 0.35f, size.height * 0.8f)
        }
        drawPath(arrowPath, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
    }
}

@Composable
private fun NotificationIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.08f

        val bellPath = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.1f)
            lineTo(size.width * 0.5f, size.height * 0.18f)
        }
        drawPath(bellPath, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.2f, size.height * 0.25f),
            size = Size(size.width * 0.6f, size.height * 0.5f),
            cornerRadius = CornerRadius(size.width * 0.15f),
            style = Stroke(width = strokeWidth)
        )

        drawLine(
            color = color,
            start = Offset(size.width * 0.15f, size.height * 0.75f),
            end = Offset(size.width * 0.85f, size.height * 0.75f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        drawCircle(
            color = color,
            radius = size.width * 0.08f,
            center = Offset(size.width * 0.5f, size.height * 0.88f)
        )
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

// 배너 고정 높이
private val BannerHeight = 380.dp

// ===== TOP 배너 (KREAM 스타일) =====
@Composable
private fun TopBannerSection(
    bannerItems: List<BoxOfficeItem>,
    onBannerClick: (BoxOfficeItem) -> Unit
) {
    val actualItemCount = bannerItems.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BannerHeight)
            .background(Color(0xFFF5F5F5))
    ) {
        if (bannerItems.isEmpty()) {
            // 로딩 상태
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "인기 공연을 불러오는 중...",
                    fontSize = 14.sp,
                    color = BaeminGray
                )
            }
        } else {
            val pageCount = Int.MAX_VALUE
            val initialPage = (pageCount / 2) - ((pageCount / 2) % actualItemCount)
            val pagerState = rememberPagerState(
                initialPage = initialPage,
                pageCount = { pageCount }
            )

            // 자동 스크롤 (3초마다, 유저 터치 시 2초 후 재시작)
            LaunchedEffect(pagerState) {
                var lastInteractionTime = 0L

                while (true) {
                    val currentTime = System.currentTimeMillis()

                    // 유저가 스크롤 중이면 마지막 상호작용 시간 갱신
                    if (pagerState.isScrollInProgress) {
                        lastInteractionTime = currentTime
                        delay(100L)
                        continue
                    }

                    // 마지막 상호작용 후 2초가 지났는지 확인
                    val timeSinceLastInteraction = currentTime - lastInteractionTime
                    if (lastInteractionTime > 0 && timeSinceLastInteraction < 2000L) {
                        delay(100L)
                        continue
                    }

                    // 3초 대기 후 다음 페이지로 스크롤
                    delay(3000L)

                    // 스크롤 중이 아닐 때만 자동 스크롤
                    if (!pagerState.isScrollInProgress) {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val actualIndex = page % actualItemCount
                val item = bannerItems[actualIndex]

                BannerItem(
                    item = item,
                    onClick = { onBannerClick(item) }
                )
            }

            // 인디케이터 바 (하단) - 프로그레스 바 스타일
            val currentIndex = pagerState.currentPage % actualItemCount
            val progress = (currentIndex + 1).toFloat() / actualItemCount.toFloat()

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 배경 바
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                )
                // 진행 바
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
private fun BannerItem(
    item: BoxOfficeItem,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        // 배경 이미지 (고화질, 전체 표시)
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(item.posterUrl)
                .crossfade(true)
                .size(coil.size.Size.ORIGINAL)
                .build(),
            contentDescription = item.performanceName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // 그라데이션 오버레이 (하단)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 300f
                    )
                )
        )

        // 텍스트 정보 (좌측 하단)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 32.dp, end = 16.dp)
        ) {
            // 공연 이름 (크게)
            Text(
                text = item.performanceName ?: "",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 공연장
            Text(
                text = item.placeName ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // 기간 (startDate ~ endDate)
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// 색상의 밝기(휘도) 계산
private fun calculateLuminance(color: Int): Double {
    val red = android.graphics.Color.red(color) / 255.0
    val green = android.graphics.Color.green(color) / 255.0
    val blue = android.graphics.Color.blue(color) / 255.0

    val r = if (red <= 0.03928) red / 12.92 else Math.pow((red + 0.055) / 1.055, 2.4)
    val g = if (green <= 0.03928) green / 12.92 else Math.pow((green + 0.055) / 1.055, 2.4)
    val b = if (blue <= 0.03928) blue / 12.92 else Math.pow((blue + 0.055) / 1.055, 2.4)

    return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

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
    onFestivalTabSelected: (SignGuCode) -> Unit,
    onFestivalItemClick: (PerformanceInfoItem) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        // TOP 배너 (KREAM 스타일)
        item {
            TopBannerSection(
                bannerItems = state.rankList.take(6),
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
                onRefreshClick = onRefreshNearbyClick,
                onItemClick = onNearbyItemClick
            )
        }

        // 내 주변 공연 전체보기 버튼
        item {
            ViewAllMyAreaPerformancesButton(
                onClick = {
                    // TODO: 내 주변 공연 전체보기 페이지 연결
                    Log.d(TAG, "View All My Area Performances button clicked")
                }
            )
        }

        // 장르별 랭킹 섹션
        item {
            GenreRankSection(
                categories = state.categories,
                selectedGenreTab = state.selectedGenreTab,
                genreRankList = state.genreRankList,
                onGenreTabSelected = onGenreTabSelected,
                onItemClick = onGenreRankItemClick
            )
        }

        // 인기 순위 제목
        item {
            Text(
                text = "${state.currentMonth}월 인기 순위 Top50",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BaeminDarkGray,
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
                        color = BaeminGray
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
                onFestivalTabSelected = onFestivalTabSelected,
                onItemClick = onFestivalItemClick
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
            color = BaeminDarkGray,
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
            Text(
                text = "${currentMonth}월 ${signGuCode.displayName} 공연",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BaeminDarkGray
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
                        .imageInfo(R.drawable.ic_refresh)
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
                    text = "주변 공연 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = BaeminGray
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
            color = BaeminDarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = BaeminGray,
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
                color = BaeminPrimary,
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
    onGenreTabSelected: (GenreCode) -> Unit,
    onItemClick: (BoxOfficeItem) -> Unit
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
                color = BaeminDarkGray
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        // TODO: 장르 랭킹 리스트 화면 이동
                    },
                contentAlignment = Alignment.Center
            ) {
                ArrowRightIcon(
                    color = BaeminGray,
                    modifier = Modifier.size(20.dp)
                )
            }
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
                    text = "랭킹 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = BaeminGray
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
    onFestivalTabSelected: (SignGuCode) -> Unit,
    onItemClick: (PerformanceInfoItem) -> Unit
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
                color = BaeminDarkGray
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        // TODO: 축제리스트 페이지 이동
                    },
                contentAlignment = Alignment.Center
            ) {
                ArrowRightIcon(
                    color = BaeminGray,
                    modifier = Modifier.size(20.dp)
                )
            }
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
                    text = "축제 정보를 불러오는 중...",
                    fontSize = 14.sp,
                    color = BaeminGray
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
                color = if (isSelected) BaeminPrimary else BaeminBackground
            )
            .border(
                width = 1.dp,
                color = BaeminPrimary,
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
            color = if (isSelected) Color.White else BaeminPrimary
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
            color = BaeminDarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = BaeminGray,
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
                color = BaeminPrimary,
                maxLines = 1,
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
                color = if (isSelected) BaeminPrimary else BaeminBackground
            )
            .border(
                width = 1.dp,
                color = BaeminPrimary,
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
            color = if (isSelected) Color.White else BaeminPrimary
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
                            else -> BaeminDarkGray.copy(alpha = 0.8f)
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
            color = BaeminDarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = BaeminGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        if (!item.performancePeriod.isNullOrBlank()) {
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 10.sp,
                color = BaeminPrimary,
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
                            else -> BaeminDarkGray.copy(alpha = 0.8f)
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
            color = BaeminDarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 장소명
        Text(
            text = item.placeName ?: "",
            fontSize = 11.sp,
            color = BaeminGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 공연 기간
        if (!item.performancePeriod.isNullOrBlank()) {
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 10.sp,
                color = BaeminPrimary,
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
            .border(1.dp, BaeminPrimary, RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "내 주변 공연 전체보기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BaeminPrimary
        )
        Spacer(modifier = Modifier.width(4.dp))
        // Placeholder for arrow icon, ideally replace with an actual icon resource
        Text(
            text = ">", // Using text for now, can be replaced with an actual icon
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BaeminPrimary
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
                color = if (isSelected) BaeminPrimary else BaeminBackground
            )
            .border(
                width = 1.dp,
                color = BaeminPrimary,
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
            color = if (isSelected) Color.White else BaeminPrimary
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
            else -> BaeminDarkGray
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
                color = BaeminDarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 장소명
            Text(
                text = item.placeName ?: "",
                fontSize = 13.sp,
                color = BaeminGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 공연 기간
            Text(
                text = item.performancePeriod ?: "",
                fontSize = 12.sp,
                color = BaeminPrimary,
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
            color = BaeminGray
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
                color = BaeminDarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = BaeminGray,
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
        onFestivalItemClick = {},
        onFestivalTabSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LoungeHeaderPreview() {
    LoungeHeader(
        onSearchClick = {},
        onNotificationClick = {}
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

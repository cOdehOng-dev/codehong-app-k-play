package com.codehong.app.kplay.ui.lounge

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.codehong.app.kplay.R
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.widget.image.HongImageBuilder
import com.codehong.library.widget.image.HongImageCompose

private const val TAG = "LoungeScreen"

// 배민 스타일 컬러
private val BaeminPrimary = Color(0xFF2AC1BC)
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
    onNearbyItemClick: (PerformanceInfoItem) -> Unit
) {
    Scaffold(
        containerColor = BaeminBackground,
        topBar = {
            LoungeHeader(
                onSearchClick = {
                    Log.d(TAG, "Search icon clicked")
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
                    onNearbyItemClick = onNearbyItemClick
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "K-Play",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BaeminPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                SearchIcon(
                    color = BaeminDarkGray,
                    modifier = Modifier.size(24.dp)
                )
            }
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
@Composable
private fun HomeContent(
    state: LoungeState,
    onCategoryClick: (GenreCode) -> Unit,
    onRankTabSelected: (RankTab) -> Unit,
    onRankItemClick: (BoxOfficeItem) -> Unit,
    onRefreshNearbyClick: () -> Unit,
    onNearbyItemClick: (PerformanceInfoItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // 배너
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F8F7)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "오늘의 추천 공연",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BaeminPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "뮤지컬, 연극, 콘서트 등 다양한 공연을 만나보세요",
                        fontSize = 14.sp,
                        color = BaeminGray
                    )
                }
            }
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

        // 순위 리스트
        val filteredRankList = state.rankList.filter { item ->
            val rank = item.rank?.toIntOrNull() ?: 0
            rank in state.selectedRankTab.startRank..state.selectedRankTab.endRank
        }

        itemsIndexed(
            items = filteredRankList,
            key = { _, item -> item.performanceId ?: item.hashCode() }
        ) { index, item ->
            RankListItem(
                item = item,
                onClick = { onRankItemClick(item) }
            )
            if (index < filteredRankList.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color(0xFFEEEEEE)
                )
            }
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentMonth}월 내 주변 공연",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BaeminDarkGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF0F8F7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = signGuCode.displayName,
                        fontSize = 12.sp,
                        color = BaeminPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            HongImageCompose(
                HongImageBuilder()
                    .width(15)
                    .height(15)
                    .imageInfo(R.drawable.ic_refresh)
                    .onClick {
                        onRefreshClick()
                    }
                    .applyOption()
            )
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
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = myAreaList,
                    key = { _, item -> item.id ?: item.hashCode() }
                ) { _, item ->
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
        onNearbyItemClick = {}
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

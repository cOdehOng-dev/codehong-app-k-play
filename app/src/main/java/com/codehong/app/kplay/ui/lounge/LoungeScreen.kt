package com.codehong.app.kplay.ui.lounge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codehong.app.kplay.domain.model.BottomTabItem
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.ui.lounge.content.MyLocationContent
import com.codehong.app.kplay.ui.lounge.content.SettingContent
import com.codehong.app.kplay.ui.lounge.content.home.HomeContent
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


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
        onThemeChanged = { themeType ->
            viewModel.setEvent(LoungeEvent.OnThemeChanged(themeType))
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
        },
        onFavoriteItemClick = { id ->
            viewModel.setEvent(LoungeEvent.OnFavoriteItemClick(id))
        },
        onFavoriteItemDelete = { id ->
            viewModel.setEvent(LoungeEvent.OnFavoriteItemDelete(id))
        }
    )
}

@Composable
private fun LoungeScreenContent(
    state: LoungeState,
    onTabSelected: (BottomTabType) -> Unit,
    onThemeChanged: (ThemeType) -> Unit,
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
    onLocalMoreClick: () -> Unit,
    onFavoriteItemClick: (String) -> Unit,
    onFavoriteItemDelete: (String) -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDarkMode = when (state.themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemDark
    }

    val tabList = listOf(
        BottomTabItem(BottomTabType.HOME, R.drawable.honglib_ic_home),
        BottomTabItem(BottomTabType.MY_LOCATION, R.drawable.honglib_ic_location),
        BottomTabItem(BottomTabType.BOOKMARK, R.drawable.honglib_ic_favorite),
        BottomTabItem(BottomTabType.SETTINGS, R.drawable.honglib_ic_setting)
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .hongBackground(HongColor.WHITE_100)
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = HongColor.WHITE_100.toColor(),
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = HongColor.GRAY_10.toColor()
                )
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    NavigationBar(
                        modifier = Modifier.height(60.dp),
                        containerColor = HongColor.WHITE_100.toColor(),
                        tonalElevation = 0.dp
                    ) {
                        tabList.forEach { tabItem ->
                            val isSelected = state.selectedTab == tabItem.tab
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val scale by animateFloatAsState(
                                targetValue = if (isPressed) 0.65f else 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "tabScale"
                            )
                            NavigationBarItem(
                                modifier = Modifier.scale(scale),
                                selected = isSelected,
                                onClick = { onTabSelected(tabItem.tab) },
                                interactionSource = interactionSource,
                                icon = {
                                    HongImageCompose(
                                        HongImageBuilder()
                                            .width(26)
                                            .height(26)
                                            .imageInfo(tabItem.iconRes)
                                            .imageColor(if (isSelected) HongColor.MAIN_ORANGE_100 else HongColor.GRAY_50)
                                            .scaleType(HongScaleType.CENTER_CROP)
                                            .applyOption()
                                    )
                                },
                                label = {
                                    HongTextCompose(
                                        option = HongTextBuilder()
                                            .text(tabItem.tab.label)
                                            .typography(HongTypo.BODY_13)
                                            .color(if (isSelected) HongColor.MAIN_ORANGE_100 else HongColor.GRAY_50)
                                            .applyOption()
                                    )
                                },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedTextColor = HongColor.MAIN_ORANGE_100.toColor(),
                                    indicatorColor = Color.Transparent,
                                    unselectedTextColor = HongColor.GRAY_50.toColor()
                                )
                            )
                        }
                    }
                }
            }
        }
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
                BottomTabType.BOOKMARK -> BookmarkContent(
                    favoriteList = state.favoriteList,
                    isDarkMode = isDarkMode,
                    onItemClick = onFavoriteItemClick,
                    onItemDelete = onFavoriteItemDelete
                )
                BottomTabType.SETTINGS -> SettingContent(
                    isDarkMode = isDarkMode,
                    themeType = state.themeType,
                    onThemeChanged = onThemeChanged
                )
            }
        }
    }
}

// ===== 찜 탭 콘텐츠 =====
@Composable
private fun BookmarkContent(
    favoriteList: List<FavoritePerformance>,
    isDarkMode: Boolean,
    onItemClick: (String) -> Unit,
    onItemDelete: (String) -> Unit
) {
    if (favoriteList.isEmpty()) {
        EmptyTabContent(
            title = "찜한 공연",
            description = "관심있는 공연을 찜해보세요"
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .hongBackground(if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
        ) {
            items(
                items = favoriteList,
                key = { it.id }
            ) { item ->
                SwipeToDeleteItem(
                    onDelete = { onItemDelete(item.id) }
                ) {
                    FavoritePerformanceItem(
                        item = item,
                        isDarkMode = isDarkMode,
                        onClick = { onItemClick(item.id) }
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = if (isDarkMode) HongColor.DARK_GRAY_100.toColor() else HongColor.GRAY_10.toColor()
                )
            }
        }
    }
}

@Composable
private fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val deleteButtonWidthDp = 80.dp
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidthDp.toPx() }
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val draggableState = rememberDraggableState { delta ->
        coroutineScope.launch {
            offsetX.snapTo((offsetX.value + delta).coerceIn(-deleteButtonWidthPx, 0f))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // 삭제 버튼 (배경)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(deleteButtonWidthDp)
                .fillMaxHeight()
                .background(Color.Red)
                .clickable {
                    coroutineScope.launch {
                        offsetX.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                    }
                    onDelete()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "삭제",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 콘텐츠 (전경)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        coroutineScope.launch {
                            val targetOffset = if (offsetX.value < -deleteButtonWidthPx / 2) {
                                -deleteButtonWidthPx
                            } else {
                                0f
                            }
                            offsetX.animateTo(
                                targetOffset,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium)
                            )
                        }
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
private fun FavoritePerformanceItem(
    item: FavoritePerformance,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hongBackground(if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 포스터 이미지
        HongImageCompose(
            option = HongImageBuilder()
                .width(60)
                .height(80)
                .imageInfo(item.posterUrl)
                .radius(HongRadiusInfo(all = 8))
                .scaleType(HongScaleType.CENTER_CROP)
                .applyOption()
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 공연 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 공연명
            HongTextCompose(
                option = HongTextBuilder()
                    .text(item.name ?: "")
                    .typography(HongTypo.BODY_16_B)
                    .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                    .applyOption()
            )

            if (!item.facilityName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                HongTextCompose(
                    option = HongTextBuilder()
                        .text(item.facilityName)
                        .typography(HongTypo.BODY_13)
                        .color(HongColor.GRAY_50)
                        .applyOption()
                )
            }

            val period = buildPeriod(item.startDate, item.endDate)
            if (period.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                HongTextCompose(
                    option = HongTextBuilder()
                        .text(period)
                        .typography(HongTypo.BODY_13)
                        .color(HongColor.GRAY_50)
                        .applyOption()
                )
            }
        }

        // 화살표 아이콘
        HongImageCompose(
            option = HongImageBuilder()
                .width(20)
                .height(20)
                .imageInfo(R.drawable.honglib_ic_arrow_right)
                .imageColor(HongColor.GRAY_50)
                .scaleType(HongScaleType.CENTER_CROP)
                .applyOption()
        )
    }
}

private fun buildPeriod(startDate: String?, endDate: String?): String {
    if (startDate.isNullOrBlank() && endDate.isNullOrBlank()) return ""
    val start = startDate?.let { formatDate(it) } ?: ""
    val end = endDate?.let { formatDate(it) } ?: ""
    return if (start == end) start else "$start ~ $end"
}

private fun formatDate(date: String): String {
    if (date.length != 8) return date
    return "${date.substring(0, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}"
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
        onThemeChanged = {},
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
        onLocalMoreClick = {},
        onFavoriteItemClick = {},
        onFavoriteItemDelete = {}
    )
}

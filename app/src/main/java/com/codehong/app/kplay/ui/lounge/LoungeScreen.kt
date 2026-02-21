package com.codehong.app.kplay.ui.lounge

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codehong.app.kplay.domain.model.BottomTabItem
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.ui.lounge.content.SettingContent
import com.codehong.app.kplay.ui.lounge.content.favorite.BookmarkContent
import com.codehong.app.kplay.ui.lounge.content.home.HomeContent
import com.codehong.app.kplay.ui.lounge.content.mylocation.MyLocationContent
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose


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
        },
        onCacheDeleteConfirmed = {
            viewModel.setEvent(LoungeEvent.OnCacheDeleteConfirmed)
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
    onFavoriteItemDelete: (String) -> Unit,
    onCacheDeleteConfirmed: () -> Unit
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
                    venueGroups = state.venueGroups,
                    isVenueGroupLoading = state.apiLoading.isVenueGroupLoading,
                    selectedAreaName = state.selectedSignGuCode.displayName,
                    onPerformanceClick = onNearbyItemClick
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
                    cacheSizeText = state.cacheSizeText,
                    onThemeChanged = onThemeChanged,
                    onCacheDeleteConfirmed = onCacheDeleteConfirmed
                )
            }
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
        onFavoriteItemDelete = {},
        onCacheDeleteConfirmed = {}
    )
}

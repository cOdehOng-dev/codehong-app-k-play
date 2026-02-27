package com.codehong.app.kplay.ui.genre

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.ui.common.BackHeader
import com.codehong.app.kplay.ui.common.Badge
import com.codehong.app.kplay.ui.common.ChangeDateButton
import com.codehong.app.kplay.ui.common.GenreScrollTab
import com.codehong.app.kplay.ui.lounge.content.ColumnShimmer
import com.codehong.library.util.extensions.toYyyyMmDd
import com.codehong.library.widget.button.text.HongButtonTextBuilder
import com.codehong.library.widget.button.text.HongButtonTextCompose
import com.codehong.library.widget.calendar.HongCalendarBuilder
import com.codehong.library.widget.calendar.HongCalendarCompose
import com.codehong.library.widget.calendar.HongCalendarOption
import com.codehong.library.widget.calendar.model.HongCalendarDayOfWeekLangType
import com.codehong.library.widget.calendar.model.HongCalendarInitialSelectedInfo
import com.codehong.library.widget.extensions.clickPress
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.toHexCode
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.progress.HongProgress
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongFont
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import com.codehong.library.widget.util.HongDateUtil

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
fun GenreRankListScreen(
    viewModel: GenreRankListViewModel = hiltViewModel(),
    onClickBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenreRankListScreenContent(
        state = state,
        onClickBack = onClickBack,
        onGenreCodeSelected = { genreCode ->
            viewModel.setEvent(GenreRankListEvent.OnGenreCodeSelected(genreCode))
        },
        onDateChangeClick = {
            viewModel.setEvent(GenreRankListEvent.OnDateChangeClick)
        },
        onRankItemClick = { item ->
            viewModel.setEvent(GenreRankListEvent.OnRankItemClick(item))
        },
        onLoadMore = {
            viewModel.setEvent(GenreRankListEvent.OnLoadMore)
        },
        onSelectDate = { startDate, endDate ->
            viewModel.setEvent(GenreRankListEvent.OnDateSelected(startDate, endDate))
            viewModel.hideCalendar()
        },
        onDismissCalendar = {
            viewModel.hideCalendar()
        }
    )
}

@Composable
private fun GenreRankListScreenContent(
    state: GenreRankListState,
    onClickBack: () -> Unit,
    onGenreCodeSelected: (GenreCode) -> Unit,
    onDateChangeClick: () -> Unit,
    onRankItemClick: (BoxOfficeItem) -> Unit,
    onLoadMore: () -> Unit,
    onSelectDate: (startDate: String, endDate: String) -> Unit,
    onDismissCalendar: () -> Unit = {}
) {
    val isDarkMode = when (state.themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }
    val bgColor = if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100

    val listState = rememberLazyListState()

    var selectStartDate by remember { mutableStateOf(state.startDate) }
    var selectEndDate by remember { mutableStateOf(state.endDate) }

    // Infinite scroll 감지
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && totalItems > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isLoading && !state.isLoadingMore && state.hasMoreData) {
            onLoadMore()
        }
    }

    // 탭 변경 시 스크롤 위치 초기화
    LaunchedEffect(state.selectedGenreCode) {
        listState.scrollToItem(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .hongBackground(bgColor),
            containerColor = bgColor.toColor(),
            topBar = {
                BackHeader("${state.initialGenreCode.displayName} 랭킹") { onClickBack() }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 장르 탭
                GenreScrollTab(
                    selected = state.selectedGenreCode,
                    onSelect = onGenreCodeSelected
                )

                // 날짜 선택 영역
                ChangeDateButton(
                    startDate = state.startDate,
                    endDate = state.endDate,
                    onDateChangeClick = onDateChangeClick
                )

                // 랭킹 리스트
                if (state.isLoading && state.genreRankList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ColumnShimmer()
                    }
                } else if (state.genreRankList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        HongTextCompose(
                            option = HongTextBuilder()
                                .text("공연이 없어요 :(")
                                .typography(HongTypo.BODY_17)
                                .color(HongColor.GRAY_50)
                                .applyOption()
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = state.genreRankList,
                            key = { index, item -> item.performanceId ?: index }
                        ) { index, item ->
                            GenreRankListItemContent(
                                item = item,
                                rank = index + 1,
                                onClick = { onRankItemClick(item) }
                            )
                            if (index < state.genreRankList.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 1.dp,
                                    color = HongColor.GRAY_10.toColor()
                                )
                            }
                        }


                        // 하단 여백
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
        // 로딩 인디케이터 (더보기)
        if (state.isLoadingMore) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                HongProgress()
            }
        }

        // 달력 컨텐츠
        AnimatedVisibility(
            visible = state.isShowCalendar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismissCalendar() }
            )
        }

        AnimatedVisibility(
            visible = state.isShowCalendar,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.White)
                    .navigationBarsPadding()
            ) {
                // 핸들바
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .hongBackground(HongColor.GRAY_30)
                    )
                }

                HongCalendarCompose(
                    option = HongCalendarBuilder()
                        .height(400)
                        .backgroundColor(HongColor.WHITE_100.hex)
                        .initialSelectedInfo(
                            HongCalendarInitialSelectedInfo(
                                startDate = state.startDate,
                                endDate = state.endDate
                            )
                        )
                        .dayOfWeekTextOption(
                            HongTextBuilder()
                                .size(13)
                                .color("#666666")
                                .fontType(HongFont.PRETENDARD_400)
                                .applyOption()
                        )
                        .dayOfWeekLangType(HongCalendarDayOfWeekLangType.KOR)
                        .yearMonthTextOption(
                            HongTextBuilder()
                                .size(19)
                                .color(HongColor.BLACK_100.hex)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .yearMonthPattern("yyyy.MM")
                        .startDayTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color(HongColor.WHITE_100.hex)
                                .backgroundColor(HongCalendarOption.DEFAULT_SELECT_START_DAY_BACKGROUND_COLOR)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .endDayTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color(HongColor.WHITE_100.hex)
                                .backgroundColor(HongCalendarOption.DEFAULT_SELECT_END_DAY_BACKGROUND_COLOR)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .rangeDaysTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color(HongColor.MAIN_ORANGE_100.hex)
                                .backgroundColor(HongCalendarOption.DEFAULT_SELECT_RANGE_DAYS_BACKGROUND_COLOR)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .holidaysTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color("#ff322e")
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .pastDaysTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color("#cccccc")
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .selectTodayTextOption(
                            HongTextBuilder()
                                .size(8)
                                .color(HongColor.WHITE_100.hex)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .unselectTodayTextOption(
                            HongTextBuilder()
                                .size(8)
                                .color("#545457")
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .defaultDayTextOption(
                            HongTextBuilder()
                                .size(17)
                                .color(HongColor.BLACK_100.hex)
                                .fontType(HongFont.PRETENDARD_700)
                                .applyOption()
                        )
                        .spacingHorizontal(16)
                        .bottomSpacingWeek(20)
                        .holidayList(HongDateUtil.KOREAN_HOLIDAY_LIST)
                        .dayOfWeekBottomLineColorHex("#eeeeee")
                        .onSelected { startDate, endDate ->
                            if (startDate == null || endDate == null) {
                                return@onSelected
                            }

                            selectStartDate = startDate.toYyyyMmDd()
                            selectEndDate = endDate.toYyyyMmDd()
                        }
                        .applyOption(),
                )

                HongButtonTextCompose(
                    HongButtonTextBuilder()
                        .margin(HongSpacingInfo(left = 16f, right = 16f, top = 12f, bottom = 12f))
                        .width(HongLayoutParam.MATCH_PARENT.value)
                        .height(52)
                        .text("적용하기")
                        .textTypo(HongTypo.BODY_18_B)
                        .textColor(HongColor.WHITE_100)
                        .backgroundColor(HongColor.MAIN_ORANGE_100.hex)
                        .radius(HongRadiusInfo(all = 12))
                        .onClick { onSelectDate(selectStartDate, selectEndDate) }
                        .applyOption()
                )
            }
        }
    }
}

// ===== 랭킹 리스트 아이템 =====
@Composable
private fun GenreRankListItemContent(
    item: BoxOfficeItem,
    rank: Int,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickPress(onClick = onClick)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 순위 텍스트
        Box {
            HongImageCompose(
                HongImageBuilder()
                    .width(100)
                    .height(130)
                    .radius(HongRadiusInfo(8))
                    .imageInfo(item.posterUrl)
                    .scaleType(HongScaleType.CENTER_CROP)
                    .applyOption()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                HongTextCompose(
                    option = HongTextBuilder()
                        .text("$rank")
                        .margin(
                            HongSpacingInfo(
                                left = 5f,
                                bottom = 5f
                            )
                        )
                        .typography(HongTypo.TITLE_36_B)
                        .color(
                            when (rank) {
                                1 -> GoldColor.toHexCode()
                                2 -> SilverColor.toHexCode()
                                3 -> BronzeColor.toHexCode()
                                else -> HongColor.WHITE_100.hex
                            }
                        )
                        .applyOption()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 정보 영역
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 공연명
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 8f))
                    .text(item.performanceName)
                    .typography(HongTypo.BODY_15_B)
                    .color(HongColor.BLACK_100)
                    .maxLines(2)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            // 장소명
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 6f))
                    .text(item.placeName)
                    .typography(HongTypo.BODY_13)
                    .color(HongColor.DARK_GRAY_100)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            // 공연 기간
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 6f))
                    .text(item.performancePeriod)
                    .typography(HongTypo.CONTENTS_12)
                    .color(HongColor.GRAY_50)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 카테고리, 지역 뱃지
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item.category
                    .takeIf { !it.isNullOrBlank() }
                    ?.let {
                        Badge(text = it)
                    }
                item.area
                    .takeIf { !it.isNullOrBlank() }
                    ?.let {
                        Badge(text = it)
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenreRankListScreenPreview() {
    GenreRankListScreenContent(
        state = GenreRankListState(),
        onClickBack = {},
        onGenreCodeSelected = {},
        onDateChangeClick = {},
        onRankItemClick = {},
        onLoadMore = {},
        onSelectDate = { _, _ -> }
    )
}

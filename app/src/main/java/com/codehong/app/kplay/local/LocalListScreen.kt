package com.codehong.app.kplay.local

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.ui.common.BackHeader
import com.codehong.app.kplay.ui.common.ChangeDateButton
import com.codehong.app.kplay.ui.common.PerformanceItemContent
import com.codehong.app.kplay.ui.common.RegionScrollTab
import com.codehong.app.kplay.ui.lounge.content.ColumnShimmer
import com.codehong.library.widget.button.text.HongButtonTextBuilder
import com.codehong.library.widget.button.text.HongButtonTextCompose
import com.codehong.library.widget.calendar.HongCalendarBuilder
import com.codehong.library.widget.calendar.HongCalendarCompose
import com.codehong.library.widget.calendar.HongCalendarOption
import com.codehong.library.widget.calendar.model.HongCalendarDayOfWeekLangType
import com.codehong.library.widget.calendar.model.HongCalendarInitialSelectedInfo
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.toYyyyMmDd
import com.codehong.library.widget.progress.HongProgress
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongFont
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import com.codehong.library.widget.util.HongDateUtil

@Composable
fun LocalListScreen(
    viewModel: LocalListViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LocalListScreenContent(
        state = state,
        onBackClick = onBackClick,
        onSignGuCodeSelected = { signGuCode ->
            viewModel.setEvent(LocalListEvent.OnSignGuCodeSelected(signGuCode))
        },
        onDateChangeClick = {
            viewModel.setEvent(LocalListEvent.OnDateChangeClick)
        },
        onPerformanceClick = { item ->
            viewModel.setEvent(LocalListEvent.OnPerformanceClick(item))
        },
        onLoadMore = {
            viewModel.setEvent(LocalListEvent.OnLoadMore)
        },
        onSelectDate = { startDate, endDate ->
            viewModel.setEvent(LocalListEvent.OnDateSelected(startDate, endDate))
            viewModel.hideCalendar()
        },
        onDismissCalendar = {
            viewModel.hideCalendar()
        }
    )
}

@Composable
private fun LocalListScreenContent(
    state: LocalListState,
    onBackClick: () -> Unit,
    onSignGuCodeSelected: (SignGuCode) -> Unit,
    onDateChangeClick: () -> Unit,
    onPerformanceClick: (PerformanceInfoItem) -> Unit,
    onLoadMore: () -> Unit,
    onSelectDate: (startDate: String, endDate: String) -> Unit,
    onDismissCalendar: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    var selectStartDate by remember { mutableStateOf(state.startDate) }
    var selectEndDate by remember { mutableStateOf(state.endDate) }

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
    LaunchedEffect(state.selectedSignGuCode) {
        listState.scrollToItem(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .hongBackground(HongColor.WHITE_100),
            containerColor = HongColor.WHITE_100.toColor(),
            topBar = {
                BackHeader(
                    "지역별 공연",
                    onBackClick
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 지역 탭
                RegionScrollTab(
                    selectedSignGuCode = state.selectedSignGuCode,
                    onSignGuCodeSelected = onSignGuCodeSelected
                )

                // 날짜 선택 영역
                ChangeDateButton(
                    startDate = state.startDate,
                    endDate = state.endDate,
                    onDateChangeClick = onDateChangeClick
                )

                // 공연 리스트
                if (state.isLoading && state.performanceList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ColumnShimmer()
                    }
                } else if (state.performanceList.isEmpty()) {
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
                            items = state.performanceList,
                            key = { index, item -> item.id ?: index }
                        ) { index, item ->
                            PerformanceItemContent(
                                item = item,
                                onClick = { onPerformanceClick(item) }
                            )
                            if (index < state.performanceList.lastIndex) {
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
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HongProgress()
            }
        }

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

        // 달력 컨텐츠
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

@Preview(showBackground = true)
@Composable
private fun LocalListScreenPreview() {
    LocalListScreenContent(
        state = LocalListState(),
        onBackClick = {},
        onSignGuCodeSelected = {},
        onDateChangeClick = {},
        onPerformanceClick = {},
        onLoadMore = {},
        onSelectDate = { _, _ -> }
    )
}

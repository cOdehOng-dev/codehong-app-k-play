package com.codehong.app.kplay.ui.award

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode

// 배민 스타일 컬러
private val BaeminPrimary = Color(0xFF2AC1BC)
private val BaeminBackground = Color.White
private val BaeminGray = Color(0xFF999999)
private val BaeminDarkGray = Color(0xFF333333)

@Composable
fun AwardListScreen(
    viewModel: AwardListViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AwardListScreenContent(
        state = state,
        onBackClick = onBackClick,
        onSignGuCodeSelected = { signGuCode ->
            viewModel.setEvent(AwardListEvent.OnSignGuCodeSelected(signGuCode))
        },
        onDateChangeClick = {
            viewModel.setEvent(AwardListEvent.OnDateChangeClick)
        },
        onAwardClick = { item ->
            viewModel.setEvent(AwardListEvent.OnAwardClick(item))
        },
        onLoadMore = {
            viewModel.setEvent(AwardListEvent.OnLoadMore)
        }
    )
}

@Composable
private fun AwardListScreenContent(
    state: AwardListState,
    onBackClick: () -> Unit,
    onSignGuCodeSelected: (SignGuCode) -> Unit,
    onDateChangeClick: () -> Unit,
    onAwardClick: (PerformanceInfoItem) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

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
    LaunchedEffect(state.selectedSignGuCode) {
        listState.scrollToItem(0)
    }

    Scaffold(
        containerColor = BaeminBackground,
        topBar = {
            // 헤더 제목: 수상작 리스트 (고정)
            AwardListHeader(
                title = "수상작 리스트",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 지역 탭
            SignGuCodeTabRow(
                selectedSignGuCode = state.selectedSignGuCode,
                onSignGuCodeSelected = onSignGuCodeSelected
            )

            // 날짜 선택 영역
            DateSelectionRow(
                startDate = state.startDate,
                endDate = state.endDate,
                onDateChangeClick = onDateChangeClick
            )

            // 수상작 리스트
            if (state.isLoading && state.awardList.isEmpty()) {
                // 로딩 상태
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BaeminPrimary)
                }
            } else if (state.awardList.isEmpty()) {
                // ===== Empty UI: 데이터가 없을 경우 표시 =====
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "수상작 정보가 없습니다",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = BaeminGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "다른 지역이나 기간을 선택해보세요",
                            fontSize = 14.sp,
                            color = BaeminGray
                        )
                    }
                }
                // ===== Empty UI 끝 =====
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                        items = state.awardList,
                        key = { index, item -> item.id ?: index }
                    ) { index, item ->
                        AwardListItem(
                            item = item,
                            onClick = { onAwardClick(item) }
                        )
                        if (index < state.awardList.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = Color(0xFFEEEEEE)
                            )
                        }
                    }

                    // 로딩 인디케이터 (더보기)
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = BaeminPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
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
}

// ===== Header =====
@Composable
private fun AwardListHeader(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(BaeminBackground)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                color = BaeminDarkGray
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BaeminDarkGray
        )
    }
}

// ===== 지역 탭 (타원형 스타일) =====
@Composable
private fun SignGuCodeTabRow(
    selectedSignGuCode: SignGuCode,
    onSignGuCodeSelected: (SignGuCode) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(SignGuCode.entries) { signGuCode ->
            val isSelected = signGuCode == selectedSignGuCode
            SignGuCodeTabChip(
                text = signGuCode.displayName,
                isSelected = isSelected,
                onClick = { onSignGuCodeSelected(signGuCode) }
            )
        }
    }
}

@Composable
private fun SignGuCodeTabChip(
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

// ===== 날짜 선택 영역 =====
@Composable
private fun DateSelectionRow(
    startDate: String,
    endDate: String,
    onDateChangeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 날짜 정보 (MM.dd ~ MM.dd)
        Text(
            text = formatDateDisplay(startDate, endDate),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = BaeminDarkGray
        )

        // 날짜 변경 버튼
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .clickable(onClick = onDateChangeClick)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "날짜 변경",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = BaeminPrimary
            )
        }
    }
}

// yyyyMMdd -> MM.dd 형식으로 변환
private fun formatDateDisplay(startDate: String, endDate: String): String {
    val formattedStart = formatToMMdd(startDate)
    val formattedEnd = formatToMMdd(endDate)
    return "$formattedStart ~ $formattedEnd"
}

private fun formatToMMdd(date: String): String {
    if (date.length != 8) return date
    val month = date.substring(4, 6)
    val day = date.substring(6, 8)
    return "$month.$day"
}

// ===== 수상작 리스트 아이템 =====
@Composable
private fun AwardListItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
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
            // 장르, 지역 뱃지
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

            // 공연명
            Text(
                text = item.name ?: "",
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
                text = formatPerformancePeriod(item.startDate, item.endDate),
                fontSize = 12.sp,
                color = BaeminPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 수상 정보 (<br> 태그로 구분된 뱃지)
            if (!item.awards.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                AwardBadges(awards = item.awards)
            }
        }
    }
}

private fun formatPerformancePeriod(startDate: String?, endDate: String?): String {
    if (startDate.isNullOrEmpty()) return ""

    val formattedStart = formatDateForDisplay(startDate)
    val formattedEnd = formatDateForDisplay(endDate)

    return if (formattedEnd.isNotEmpty()) {
        "$formattedStart ~ $formattedEnd"
    } else {
        formattedStart
    }
}

private fun formatDateForDisplay(date: String?): String {
    if (date.isNullOrEmpty()) return ""

    // KOPIS API는 "2026.03.21" 형식으로 반환
    if (date.contains(".")) return date

    // yyyyMMdd 형식인 경우 변환
    if (date.length == 8) {
        return "${date.substring(0, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}"
    }

    return date
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

// ===== 수상 정보 뱃지 (<br> 태그로 구분) =====
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AwardBadges(awards: String?) {
    if (awards.isNullOrBlank()) return

    // <br> 또는 <br/> 또는 <br /> 태그로 분리
    val awardList = awards
        .split(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE))
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .take(2) // 최대 2개까지만 표시

    if (awardList.isEmpty()) return

    // FlowRow: 공간이 부족하면 자동으로 다음 줄로 이동
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        awardList.forEach { award ->
            AwardBadge(text = award)
        }
    }
}

@Composable
private fun AwardBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFFFF3E0)) // 수상 뱃지용 연한 주황색 배경
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE65100) // 주황색 텍스트
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AwardListScreenPreview() {
    AwardListScreenContent(
        state = AwardListState(),
        onBackClick = {},
        onSignGuCodeSelected = {},
        onDateChangeClick = {},
        onAwardClick = {},
        onLoadMore = {}
    )
}

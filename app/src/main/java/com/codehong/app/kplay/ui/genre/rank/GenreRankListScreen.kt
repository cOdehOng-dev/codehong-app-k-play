package com.codehong.app.kplay.ui.genre.rank

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.ui.common.ChangeDateButton

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
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenreRankListScreenContent(
        state = state,
        onBackClick = onBackClick,
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
        }
    )
}

@Composable
private fun GenreRankListScreenContent(
    state: GenreRankListState,
    onBackClick: () -> Unit,
    onGenreCodeSelected: (GenreCode) -> Unit,
    onDateChangeClick: () -> Unit,
    onRankItemClick: (BoxOfficeItem) -> Unit,
    onLoadMore: () -> Unit
) {
    val isDarkMode = when (state.themeType) {
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
        ThemeType.SYSTEM -> isSystemInDarkTheme()
    }
    val bgColor = if (isDarkMode) Color(0xFF000000) else BaeminBackground
    val titleColor = if (isDarkMode) Color.White else BaeminDarkGray

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
    LaunchedEffect(state.selectedGenreCode) {
        listState.scrollToItem(0)
    }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            GenreRankListHeader(
                title = "${state.initialGenreCode.displayName} 랭킹",
                bgColor = bgColor,
                titleColor = titleColor,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 장르 탭
            GenreCodeTabRow(
                selectedGenreCode = state.selectedGenreCode,
                onGenreCodeSelected = onGenreCodeSelected
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
                    CircularProgressIndicator(color = BaeminPrimary)
                }
            } else if (state.genreRankList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "검색 결과가 없습니다.",
                        fontSize = 16.sp,
                        color = BaeminGray
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
                        GenreRankListItem(
                            item = item,
                            rank = index + 1,
                            onClick = { onRankItemClick(item) }
                        )
                        if (index < state.genreRankList.lastIndex) {
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
private fun GenreRankListHeader(
    title: String,
    bgColor: Color,
    titleColor: Color,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(bgColor)
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
                color = titleColor
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
    }
}

// ===== 장르 탭 (타원형 스타일) =====
@Composable
private fun GenreCodeTabRow(
    selectedGenreCode: GenreCode,
    onGenreCodeSelected: (GenreCode) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(GenreCode.entries) { genreCode ->
            val isSelected = genreCode == selectedGenreCode
            GenreCodeTabChip(
                text = genreCode.displayName,
                isSelected = isSelected,
                onClick = { onGenreCodeSelected(genreCode) }
            )
        }
    }
}

@Composable
private fun GenreCodeTabChip(
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

// ===== 랭킹 리스트 아이템 =====
@Composable
private fun GenreRankListItem(
    item: BoxOfficeItem,
    rank: Int,
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
        val rankColor = when (rank) {
            1 -> GoldColor
            2 -> SilverColor
            3 -> BronzeColor
            else -> BaeminDarkGray
        }

        Text(
            text = "$rank",
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
            // 장르, 지역 뱃지
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

@Preview(showBackground = true)
@Composable
private fun GenreRankListScreenPreview() {
    GenreRankListScreenContent(
        state = GenreRankListState(),
        onBackClick = {},
        onGenreCodeSelected = {},
        onDateChangeClick = {},
        onRankItemClick = {},
        onLoadMore = {}
    )
}

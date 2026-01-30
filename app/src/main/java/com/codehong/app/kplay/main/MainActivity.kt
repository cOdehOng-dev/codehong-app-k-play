package com.codehong.app.kplay.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.ui.theme.CodehongappkplayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.callPerformanceListApi()

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is MainEffect.NavigateToDetail -> {
                            Toast.makeText(
                                this@MainActivity,
                                "${effect.item.name} 상세 화면으로 이동",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            CodehongappkplayTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF2F2F7)
                ) { innerPadding ->
                    PerformanceListScreen(
                        modifier = Modifier.padding(innerPadding),
                        performances = state.performanceList,
                        onItemClick = { item ->
                            viewModel.setEvent(MainEvent.OnPerformanceClick(item))
                        }
                    )
                }
            }
        }
    }
}

private val REGION_ORDER = listOf(
    "서울", "경기", "인천",
    "부산", "대구", "광주", "대전", "울산", "세종",
    "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"
)

@Composable
fun PerformanceListScreen(
    modifier: Modifier = Modifier,
    performances: List<PerformanceInfoItem>,
    onItemClick: (PerformanceInfoItem) -> Unit
) {
    val groupedPerformances = remember(performances) {
        performances
            .groupBy { extractRegion(it.area) }
            .toSortedMap(compareBy { region ->
                val index = REGION_ORDER.indexOf(region)
                if (index >= 0) index else Int.MAX_VALUE
            })
    }

    val regions = remember(groupedPerformances) {
        groupedPerformances.keys.toList()
    }

    val tabListState = rememberLazyListState()
    val contentListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val currentVisibleRegionIndex by remember {
        derivedStateOf {
            val firstVisibleItem = contentListState.firstVisibleItemIndex
            (firstVisibleItem / 2).coerceIn(0, (regions.size - 1).coerceAtLeast(0))
        }
    }

    LaunchedEffect(currentVisibleRegionIndex) {
        if (regions.isNotEmpty()) {
            tabListState.animateScrollToItem(
                index = currentVisibleRegionIndex,
                scrollOffset = -200
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
    ) {
        if (performances.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "공연 정보를 불러오는 중...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            RegionTabRow(
                regions = regions,
                selectedIndex = currentVisibleRegionIndex,
                listState = tabListState,
                onTabClick = { index ->
                    coroutineScope.launch {
                        tabListState.animateScrollToItem(
                            index = index,
                            scrollOffset = -200
                        )
                        contentListState.animateScrollToItem(index * 2)
                    }
                }
            )

            LazyColumn(
                state = contentListState,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                groupedPerformances.forEach { (region, performanceList) ->
                    item(key = "header_$region") {
                        RegionHeader(region = region)
                    }
                    item(key = "list_$region") {
                        PerformanceHorizontalList(
                            performances = performanceList,
                            onItemClick = onItemClick
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RegionTabRow(
    regions: List<String>,
    selectedIndex: Int,
    listState: LazyListState,
    onTabClick: (Int) -> Unit
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F7))
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(regions) { index, region ->
            RegionTab(
                region = region,
                isSelected = index == selectedIndex,
                onClick = { onTabClick(index) }
            )
        }
    }
}

@Composable
fun RegionTab(
    region: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color.Black else Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = region,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color(0xFF8E8E93)
        )
    }
}

@Composable
fun RegionHeader(region: String) {
    Text(
        text = region,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 12.dp)
    )
}

@Composable
fun PerformanceHorizontalList(
    performances: List<PerformanceInfoItem>,
    onItemClick: (PerformanceInfoItem) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = performances,
            key = { it.id ?: it.hashCode() }
        ) { item ->
            PerformanceListItem(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
fun PerformanceListItem(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .height(360.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.name ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.facilityName ?: "",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.genre ?: "",
                    fontSize = 12.sp,
                    color = Color(0xFFAEAEB2),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = formatDateRange(item.startDate, item.endDate),
                fontSize = 11.sp,
                color = Color(0xFF007AFF),
                maxLines = 1
            )
        }
    }
}

private fun extractRegion(area: String?): String {
    if (area.isNullOrEmpty()) return "기타"

    return when {
        area.contains("서울") -> "서울"
        area.contains("경기") -> "경기"
        area.contains("인천") -> "인천"
        area.contains("부산") -> "부산"
        area.contains("대구") -> "대구"
        area.contains("광주") -> "광주"
        area.contains("대전") -> "대전"
        area.contains("울산") -> "울산"
        area.contains("세종") -> "세종"
        area.contains("강원") -> "강원"
        area.contains("충북") || area.contains("충청북") -> "충북"
        area.contains("충남") || area.contains("충청남") -> "충남"
        area.contains("전북") || area.contains("전라북") -> "전북"
        area.contains("전남") || area.contains("전라남") -> "전남"
        area.contains("경북") || area.contains("경상북") -> "경북"
        area.contains("경남") || area.contains("경상남") -> "경남"
        area.contains("제주") -> "제주"
        else -> "기타"
    }
}

private fun formatDateRange(startDate: String?, endDate: String?): String {
    if (startDate.isNullOrEmpty()) return ""

    val formattedStart = formatDate(startDate)
    val formattedEnd = formatDate(endDate)

    return if (formattedEnd.isNotEmpty()) {
        "$formattedStart ~ $formattedEnd"
    } else {
        formattedStart
    }
}

private fun formatDate(date: String?): String {
    if (date.isNullOrEmpty()) return ""

    // KOPIS API는 "2026.03.21" 형식으로 반환
    if (date.contains(".")) return date

    // yyyyMMdd 형식인 경우 변환
    if (date.length == 8) {
        return "${date.substring(0, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}"
    }

    return date
}

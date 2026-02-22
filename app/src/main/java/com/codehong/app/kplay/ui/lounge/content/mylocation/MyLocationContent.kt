package com.codehong.app.kplay.ui.lounge.content.mylocation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.model.performance.PerformanceGroup
import com.codehong.library.debugtool.log.TimberUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.overlay.OverlayImage
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private val PrimaryColor = Color(0xFF2AC1BC)
private val DarkGrayColor = Color(0xFF333333)
private val GrayColor = Color(0xFF999999)

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MyLocationContent(
    performanceGroups: List<PerformanceGroup>,
    isVenueGroupLoading: Boolean = false,
    selectedAreaName: String = "서울",
    onPerformanceClick: (PerformanceInfoItem) -> Unit
) {
    LaunchedEffect(performanceGroups) {
        TimberUtil.d("MyLocation ▶ venue 그룹 수: ${performanceGroups.size}")
        performanceGroups.forEach { group ->
            TimberUtil.d("MyLocation ▶ [${group.placeName}] ${group.performanceList.size}건 lat=${group.lat} lng=${group.lng}")
            group.performanceList.forEach { item ->
                TimberUtil.d("MyLocation   - id=${item.id} | name=${item.name} | ${item.startDate}~${item.endDate}")
            }
        }
    }

    val areaCenter = remember(selectedAreaName) {
        getAreaCenter(selectedAreaName)
    }

    // 각 VenueGroup에 실제 좌표 or 오프셋 좌표를 매핑
    val groupsWithPosition = remember(performanceGroups, areaCenter) {
        performanceGroups.mapIndexed { index, group ->
            val lat = group.lat ?: (areaCenter.latitude + generateOffset(group.hashCode(), index, true))
            val lng = group.lng ?: (areaCenter.longitude + generateOffset(group.hashCode(), index, false))
            group to LatLng(lat, lng)
        }
    }

    var selectedGroup by remember { mutableStateOf<PerformanceGroup?>(null) }
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(areaCenter, 11.0)
    }

    // 핀 비트맵 캐시: count별로 생성
    val bitmapCache = remember { mutableMapOf<Int, Bitmap>() }
    val overlayCache = remember { mutableMapOf<Int, OverlayImage>() }

    LaunchedEffect(selectedPosition) {
        selectedPosition?.let {
            cameraPositionState.move(CameraUpdate.scrollTo(it))
        }
    }

    LaunchedEffect(areaCenter) {
        cameraPositionState.move(CameraUpdate.scrollTo(areaCenter))
    }

    // 선택 그룹이 바뀌면 pager를 0으로 리셋하기 위한 상태
    val pagerState = rememberPagerState(initialPage = 0) {
        selectedGroup?.performanceList?.size ?: 0
    }

    LaunchedEffect(selectedGroup?.placeName) {
        pagerState.scrollToPage(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                isZoomControlEnabled = false,
                isLocationButtonEnabled = false,
                isScaleBarEnabled = false,
                isLogoClickEnabled = false
            ),
            onMapClick = { _, _ ->
                selectedGroup = null
                selectedPosition = null
            }
        ) {
            groupsWithPosition.forEach { (group, position) ->
                key(group.placeName) {
                    val isSelected = selectedGroup?.placeName == group.placeName
                    val count = group.performanceList.size

                    val overlayImage = remember(count) {
                        overlayCache.getOrPut(count) {
                            val bitmap = bitmapCache.getOrPut(count) {
                                createVenueBitmap(sizePx = 80, color = 0xFF2AC1BC.toInt(), count = count)
                            }
                            OverlayImage.fromBitmap(bitmap)
                        }
                    }

                    Marker(
                        state = rememberMarkerState(position = position),
                        icon = overlayImage,
                        width = if (isSelected) 52.dp else 44.dp,
                        height = if (isSelected) 52.dp else 44.dp,
                        onClick = {
                            selectedGroup = group
                            selectedPosition = position
                            true
                        }
                    )
                }
            }
        }

        // 위경도 조회 로딩 인디케이터
        if (isVenueGroupLoading) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = PrimaryColor,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "공연장 위치 조회 중...",
                        fontSize = 12.sp,
                        color = DarkGrayColor
                    )
                }
            }
        }

        // 하단 공연 정보 Pager 카드
        AnimatedVisibility(
            visible = selectedGroup != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        ) {
            selectedGroup?.let { group ->
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 40.dp),
                    pageSpacing = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val item = group.performanceList.getOrNull(page) ?: return@HorizontalPager
                    PerformanceInfoCard(
                        item = item,
                        onClick = { onPerformanceClick(item) }
                    )
                }
            }
        }
    }
}

// ===== 하단 공연 정보 카드 =====
@Composable
private fun PerformanceInfoCard(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(80.dp, 104.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!item.genre.isNullOrBlank()) {
                    MapSmallBadge(text = item.genre)
                }
                if (!item.area.isNullOrBlank()) {
                    MapSmallBadge(text = item.area)
                }
            }

            Text(
                text = item.name ?: "",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkGrayColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.placeName ?: "",
                fontSize = 13.sp,
                color = GrayColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.period,
                fontSize = 12.sp,
                color = PrimaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MapSmallBadge(text: String?) {
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
            color = GrayColor
        )
    }
}

// ===== 공연장 핀 비트맵 생성 (뱃지 포함) =====
private fun createVenueBitmap(sizePx: Int, color: Int, count: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val center = sizePx / 2f
    val borderWidth = sizePx * 0.12f
    val radius = center - borderWidth / 2f

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }
    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    canvas.drawCircle(center, center, radius, fillPaint)
    canvas.drawCircle(center, center, radius, borderPaint)

    // 2개 이상 공연이 있을 때 우측 상단에 빨간 뱃지 표시
    if (count > 1) {
        val badgeRadius = sizePx * 0.22f
        val badgeCenterX = sizePx * 0.78f
        val badgeCenterY = sizePx * 0.22f

        val badgeFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = android.graphics.Color.RED
            style = Paint.Style.FILL
        }
        val badgeBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = android.graphics.Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = sizePx * 0.06f
        }

        canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, badgeFill)
        canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, badgeBorder)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = android.graphics.Color.WHITE
            textSize = sizePx * 0.22f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val text = if (count > 9) "9+" else count.toString()
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, badgeCenterX, badgeCenterY + textBounds.height() / 2f, textPaint)
    }

    return bitmap
}

// ===== 좌표가 없는 아이템을 위한 오프셋 생성 =====
private fun generateOffset(hashCode: Int, index: Int, isLat: Boolean): Double {
    val seed = if (isLat) hashCode else hashCode * 31 + index
    val angle = (index * 137.5 + abs(seed) % 360) * Math.PI / 180
    val distance = 0.008 + (abs(seed) % 100) * 0.0004
    return if (isLat) distance * cos(angle) else distance * sin(angle)
}

// ===== 지역별 중심 좌표 =====
private fun getAreaCenter(areaName: String): LatLng {
    return when {
        areaName.contains("서울") -> LatLng(37.5666805, 126.9784147)
        areaName.contains("부산") -> LatLng(35.1795543, 129.0756416)
        areaName.contains("대구") -> LatLng(35.8714354, 128.601445)
        areaName.contains("인천") -> LatLng(37.4563, 126.7052)
        areaName.contains("광주") -> LatLng(35.1595, 126.8526)
        areaName.contains("대전") -> LatLng(36.3504, 127.3845)
        areaName.contains("울산") -> LatLng(35.5384, 129.3114)
        areaName.contains("세종") -> LatLng(36.4800, 127.0000)
        areaName.contains("경기") -> LatLng(37.4138, 127.5183)
        areaName.contains("강원") -> LatLng(37.8228, 128.1555)
        areaName.contains("충북") -> LatLng(36.6357, 127.4912)
        areaName.contains("충남") -> LatLng(36.5184, 126.8000)
        areaName.contains("전북") -> LatLng(35.8468, 127.1297)
        areaName.contains("전남") -> LatLng(34.8679, 126.9910)
        areaName.contains("경북") -> LatLng(36.4919, 128.8889)
        areaName.contains("경남") -> LatLng(35.4606, 128.2132)
        areaName.contains("제주") -> LatLng(33.4996, 126.5312)
        else -> LatLng(37.5666805, 126.9784147)
    }
}

package com.codehong.app.kplay.ui.lounge.content

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
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
    myAreaList: List<PerformanceInfoItem>,
    selectedAreaName: String = "서울"
) {
    val areaCenter = remember(selectedAreaName) {
        getAreaCenter(selectedAreaName)
    }

    val itemsWithPosition = remember(myAreaList, areaCenter) {
        myAreaList.mapIndexed { index, item ->
            val lat = item.latitude
                ?: (areaCenter.latitude + generateOffset(item.hashCode(), index, true))
            val lng = item.longitude
                ?: (areaCenter.longitude + generateOffset(item.hashCode(), index, false))
            item to LatLng(lat, lng)
        }
    }

    var selectedItem by remember { mutableStateOf<PerformanceInfoItem?>(null) }
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(areaCenter, 11.0)
    }

    val dotBitmap = remember { createCircleBitmap(80, 0xFF2AC1BC.toInt()) }
    val dotImage = remember { OverlayImage.fromBitmap(dotBitmap) }

    LaunchedEffect(selectedPosition) {
        selectedPosition?.let {
            cameraPositionState.move(CameraUpdate.scrollTo(it))
        }
    }

    LaunchedEffect(areaCenter) {
        cameraPositionState.move(CameraUpdate.scrollTo(areaCenter))
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
                selectedItem = null
                selectedPosition = null
            }
        ) {
            itemsWithPosition.forEach { (item, position) ->
                key(item.id ?: item.hashCode()) {
                    val isSelected = selectedItem?.id == item.id

                    Marker(
                        state = rememberMarkerState(position = position),
                        icon = dotImage,
                        width = if (isSelected) 12.dp else 22.dp,
                        height = if (isSelected) 12.dp else 22.dp,
                        onClick = {
                            selectedItem = item
                            selectedPosition = position
                            true
                        }
                    )
                }
            }
        }

        // 말풍선 (카메라가 선택된 핀 중심으로 이동하므로 화면 중앙 위에 표시)
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-60).dp)
        ) {
            selectedItem?.let { item ->
                SpeechBubble(
                    name = item.name ?: "",
                    place = item.placeName ?: ""
                )
            }
        }

        // 하단 공연 정보 카드
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 100.dp)
        ) {
            selectedItem?.let { item ->
                PerformanceInfoCard(
                    item = item,
                    onClick = {
                        // TODO: 공연 상세 화면으로 이동
                    }
                )
            }
        }
    }
}

// ===== 말풍선 =====
@Composable
private fun SpeechBubble(
    name: String,
    place: String
) {
    Box(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkGrayColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = place,
                fontSize = 11.sp,
                color = GrayColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ===== 하단 공연 정보 카드 (FestivalListItem 스타일) =====
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
        // 포스터 이미지
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.name,
            modifier = Modifier
                .size(80.dp, 104.dp)
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
                    MapSmallBadge(text = item.genre)
                }
                if (!item.area.isNullOrBlank()) {
                    MapSmallBadge(text = item.area)
                }
            }

            // 공연명
            Text(
                text = item.name ?: "",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkGrayColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 장소명
            Text(
                text = item.placeName ?: "",
                fontSize = 13.sp,
                color = GrayColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 공연 기간
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

// ===== 원형 점 비트맵 생성 =====
private fun createCircleBitmap(sizePx: Int, color: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val center = sizePx / 2f
    val borderWidth = sizePx * 0.12f
    val radius = center - borderWidth / 2f

    // 흰색 외곽선
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    // 채우기
    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    canvas.drawCircle(center, center, radius, fillPaint)
    canvas.drawCircle(center, center, radius, borderPaint)
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

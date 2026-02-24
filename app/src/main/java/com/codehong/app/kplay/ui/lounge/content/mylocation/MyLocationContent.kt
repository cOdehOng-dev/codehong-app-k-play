package com.codehong.app.kplay.ui.lounge.content.mylocation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.performance.PerformanceGroup
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.util.fullArea
import com.codehong.app.kplay.manager.BitmapManager
import com.codehong.app.kplay.ui.common.PerformanceItemContent
import com.codehong.app.kplay.util.Util
import com.codehong.app.kplay.util.Util.centerAreaLatLng
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.disableRippleClickable
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
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberUpdatedMarkerState
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

private const val SEOUL_CITY_HALL_LAT = 37.5665
private const val SEOUL_CITY_HALL_LNG = 126.9780

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MyLocationContent(
    performanceGroupList: List<PerformanceGroup>,
    isPlaceGroupLoading: Boolean = false,
    selectedAreaName: String = "서울",
    userLat: Double? = null,
    userLng: Double? = null,
    onPerformanceClick: (PerformanceInfoItem) -> Unit
) {

    val areaCenter = remember(selectedAreaName) {
        selectedAreaName.centerAreaLatLng()
    }

    // 각 VenueGroup에 실제 좌표 or 오프셋 좌표를 매핑
    val groupsWithPosition = remember(performanceGroupList, areaCenter) {
        performanceGroupList.mapIndexed { index, group ->
            val lat = group.lat ?: (areaCenter.latitude + Util.generateOffset(group.hashCode(), index, true))
            val lng = group.lng ?: (areaCenter.longitude + Util.generateOffset(group.hashCode(), index, false))
            group to LatLng(lat, lng)
        }
    }

    var selectedGroup by remember { mutableStateOf<PerformanceGroup?>(null) }
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(areaCenter, 11.0)
    }

    // count > 1 공연장에 표시할 뱃지 OverlayImage 캐시
    val badgeCache = remember { mutableMapOf<Int, OverlayImage>() }

    val locationSource = rememberFusedLocationSource()

    LaunchedEffect(selectedPosition) {
        selectedPosition?.let {
            cameraPositionState.move(CameraUpdate.scrollTo(it))
        }
    }

    LaunchedEffect(areaCenter) {
        cameraPositionState.move(CameraUpdate.scrollTo(areaCenter))
    }

    // 공연 그룹 로드 완료 시 기준 위치에서 가장 가까운 핀으로 이동
    // 위치 권한 있음: userLat/userLng 사용, 없음: 서울시청 기준
    LaunchedEffect(performanceGroupList) {
        if (groupsWithPosition.isNotEmpty()) {
            val refLat = userLat ?: SEOUL_CITY_HALL_LAT
            val refLng = userLng ?: SEOUL_CITY_HALL_LNG
            val nearest = groupsWithPosition.minByOrNull { (_, pos) ->
                val dLat = pos.latitude - refLat
                val dLng = pos.longitude - refLng
                dLat * dLat + dLng * dLng
            }?.second
            nearest?.let {
                cameraPositionState.move(CameraUpdate.scrollTo(it))
            }
        }
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
            locationSource = locationSource,
            properties = MapProperties(locationTrackingMode = LocationTrackingMode.NoFollow),
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

                    // 마커 크기 (선택 여부에 따라)
                    val markerWidthDp = if (isSelected) 40f else 36f
                    val markerHeightDp = if (isSelected) 54f else 44f

                    // 메인 핀 마커 (네이버 기본 핀, 오렌지 색상)
                    Marker(
                        state = rememberUpdatedMarkerState(position = position),
                        icon = MarkerIcons.BLACK,
                        iconTintColor = HongColor.MAIN_ORANGE_100.toColor(),
                        width = markerWidthDp.dp,
                        height = markerHeightDp.dp,
                        onClick = {
                            selectedGroup = group
                            selectedPosition = position
                            true
                        }
                    )

                    if (count > 1) {
                        val badgeIcon = remember(count) {
                            badgeCache.getOrPut(count) {
                                OverlayImage.fromBitmap(BitmapManager.createBadgeOverlayBitmap(count))
                            }
                        }
                        Marker(
                            state = rememberUpdatedMarkerState(position = position),
                            icon = badgeIcon,
                            anchor = Offset(0.35f, 1.0f),
                            width = markerWidthDp.dp,
                            height = markerHeightDp.dp,
                            zIndex = 1,
                            onClick = {
                                selectedGroup = group
                                selectedPosition = position
                                true
                            }
                        )
                    }
                }
            }
        }

        // 상단 상태 pill: 로딩 중 / 현재 지역 공연 표시
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            // 공연장 위치 조회 로딩 인디케이터
            AnimatedVisibility(
                visible = isPlaceGroupLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
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
                            color = HongColor.MAIN_ORANGE_100.toColor(),
                            strokeWidth = 2.dp
                        )
                        HongTextCompose(
                            HongTextBuilder()
                                .text("공연장 위치 조회 중...")
                                .typography(HongTypo.CONTENTS_12)
                                .color(HongColor.DARK_GRAY_100)
                                .applyOption()
                        )
                    }
                }
            }

            // 로딩 완료 후 현재 지역 공연 pill
            AnimatedVisibility(
                visible = !isPlaceGroupLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .hongBackground(
                            color = HongColor.WHITE_90,
                            radius = HongRadiusInfo(20)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HongImageCompose(
                            HongImageBuilder()
                                .width(16)
                                .height(16)
                                .imageInfo(R.drawable.honglib_ic_location)
                                .scaleType(HongScaleType.CENTER_CROP)
                                .applyOption()
                        )

                        HongTextCompose(
                            HongTextBuilder()
                                .text(selectedAreaName.fullArea())
                                .typography(HongTypo.CONTENTS_12)
                                .color(HongColor.DARK_GRAY_100)
                                .applyOption()
                        )
                    }
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
                .padding(bottom = 20.dp)
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

@Composable
private fun PerformanceInfoCard(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .hongBackground(
                color = HongColor.WHITE_100,
                radius = HongRadiusInfo(16)
            )
            .disableRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        PerformanceItemContent(item = item)
    }
}

// ===== Preview =====

private val previewPerformanceItems = listOf(
    PerformanceInfoItem(
        id = "PF001",
        name = "뮤지컬 레미제라블",
        startDate = "2026.01.01",
        endDate = "2026.03.31",
        placeName = "블루스퀘어 신한카드홀",
        area = "서울",
        genre = "뮤지컬"
    ),
    PerformanceInfoItem(
        id = "PF002",
        name = "연극 햄릿",
        startDate = "2026.02.01",
        endDate = "2026.04.30",
        placeName = "블루스퀘어 신한카드홀",
        area = "서울",
        genre = "연극"
    )
)

private val previewPerformanceGroups = listOf(
    PerformanceGroup(
        placeName = "블루스퀘어 신한카드홀",
        lat = 37.5280,
        lng = 126.9847,
        performanceList = previewPerformanceItems
    ),
    PerformanceGroup(
        placeName = "LG아트센터 서울",
        lat = 37.5115,
        lng = 127.0595,
        performanceList = listOf(
            PerformanceInfoItem(
                id = "PF003",
                name = "오케스트라 정기공연",
                startDate = "2026.02.15",
                endDate = "2026.02.28",
                placeName = "LG아트센터 서울",
                area = "서울",
                genre = "클래식"
            )
        )
    )
)

@OptIn(ExperimentalNaverMapApi::class)
@Preview(showBackground = true, name = "공연장 위치 조회 중")
@Composable
private fun MyLocationContentLoadingPreview() {
    MyLocationContent(
        performanceGroupList = emptyList(),
        isPlaceGroupLoading = true,
        selectedAreaName = "서울",
        onPerformanceClick = {}
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Preview(showBackground = true, name = "공연장 로딩 완료")
@Composable
private fun MyLocationContentLoadedPreview() {
    MyLocationContent(
        performanceGroupList = previewPerformanceGroups,
        isPlaceGroupLoading = false,
        selectedAreaName = "서울",
        onPerformanceClick = {}
    )
}

@Preview(showBackground = true, name = "공연 정보 카드")
@Composable
private fun PerformanceInfoCardPreview() {
    PerformanceInfoCard(
        item = previewPerformanceItems.first(),
        onClick = {}
    )
}

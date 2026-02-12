package com.codehong.app.kplay.ui.performance.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongSpacing
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PerformanceDetailPlaceContent(
    placeDetail: PlaceDetail?,
) {
    val latitude = placeDetail?.latitude?.toDoubleOrNull()
    val longitude = placeDetail?.longitude?.toDoubleOrNull()
    if (placeDetail == null || latitude == null || longitude == null) return

    // 서울 시청 기본 좌표 (또는 전달받은 좌표)
    val position = LatLng(latitude, longitude)

    val mapUiSettings = remember {
        MapUiSettings(
            isLocationButtonEnabled = false, // 현위치 버튼 보이기
            isZoomControlEnabled = false,   // 줌 버튼(+/-) 숨기기
            isCompassEnabled = true,        // 나침반 보이기
            isScaleBarEnabled = false,      // 축척 바 숨기기
            isLogoClickEnabled = false      // 네이버 로고 클릭 이벤트 비활성화
        )
    }

    val mapProperties = remember {
        MapProperties(
            isLiteModeEnabled = true
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition(position, 16.0)
    }

    HorizontalDivider(
        thickness = 8.dp,
        color = HongColor.GRAY_10.toColor()
    )

    Spacer(modifier = Modifier.height(16.dp))

    HongTextCompose(
        option = HongTextBuilder()
            .padding(HongSpacingInfo(left  = 16f, right = 16f))
            .text("공연장소")
            .typography(HongTypo.BODY_18_B)
            .color(HongColor.BLACK_100)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(26.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        NaverMap(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .hongBackground(
                    color = HongColor.TRANSPARENT,
                    radius = HongRadiusInfo(16)
                ),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {
            Marker(
                state = rememberUpdatedMarkerState(position = position),
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hongSpacing(HongSpacingInfo(left = 16f, right = 16f, top = 15f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HongImageCompose(
            HongImageBuilder()
                .width(18)
                .height(18)
                .imageInfo(R.drawable.honglib_ic_location)
                .applyOption()
        )
        HongTextCompose(
            option = HongTextBuilder()
                .margin(HongSpacingInfo(left = 5f))
                .text(placeDetail.placeAddress)
                .typography(HongTypo.BODY_14_B)
                .color(HongColor.BLACK_100)
                .maxLines(1)
                .overflow(HongTextOverflow.ELLIPSIS)
                .applyOption()
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}
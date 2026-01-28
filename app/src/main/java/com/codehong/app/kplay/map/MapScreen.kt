package com.codehong.app.kplay.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen(
    title: String,
    description: String,
    initialCameraPosition: LatLng? = null,
    myLocation: LatLng? = null,
    onMyLocationClick: () -> Unit = {}
) {
    // 서울 시청 기본 좌표
    val defaultPosition = LatLng(37.5666805, 126.9784147)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialCameraPosition ?: defaultPosition, 14.0)
    }

    // 초기 카메라 위치가 변경되면 이동
    LaunchedEffect(initialCameraPosition) {
        initialCameraPosition?.let {
            cameraPositionState.move(CameraUpdate.scrollTo(it))
        }
    }

    // 내 위치 버튼 클릭 시 이동
    LaunchedEffect(myLocation) {
        myLocation?.let {
            cameraPositionState.move(CameraUpdate.scrollTo(it))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            myLocation?.let { location ->
                // 바깥쪽 원 (정확도 범위)
                CircleOverlay(
                    center = location,
                    radius = 50.0,
                    color = Color(0x220066FF),
                    outlineColor = Color(0xFF0066FF),
                    outlineWidth = 1.dp
                )
                // 안쪽 원 (현재 위치 점)
                CircleOverlay(
                    center = location,
                    radius = 8.0,
                    color = Color(0xFF0066FF),
                    outlineColor = Color.White,
                    outlineWidth = 2.dp
                )
            }
        }

        FloatingActionButton(
            onClick = onMyLocationClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp),
            shape = CircleShape,
            containerColor = Color.White
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                contentDescription = "내 위치",
                tint = Color(0xFF1976D2)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(text = description)
            }
        }
    }
}
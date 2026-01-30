package com.codehong.app.kplay.ui.performance.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codehong.library.network.debug.TimberUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceDetailScreen(
    state: PerformanceDetailState,
    onEvent: (PerformanceDetailEvent) -> Unit
) {
    val detail = state.performanceDetail

    TimberUtil.d("test here detail = $detail")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = detail?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PerformanceDetailEvent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            // 플로팅 스타일 예매하기 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        // TODO: 예매하기 버튼 클릭 이벤트
                        // 이 이벤트를 통해 예매 페이지로 이동하거나 예매처 선택 다이얼로그를 표시할 수 있습니다.
                        onEvent(PerformanceDetailEvent.OnBookingClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2DB400) // 배민 스타일 그린 컬러
                    )
                ) {
                    Text(
                        text = "예매하기",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (detail != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                // 상단 포스터 이미지 (풀 블리드, 4:5 비율)
                AsyncImage(
                    model = detail.posterUrl,
                    contentDescription = "공연 포스터",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 5f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 공연명
                Text(
                    text = detail.name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 가격 정보
                if (!detail.priceInfo.isNullOrEmpty()) {
                    Text(
                        text = detail.priceInfo ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE53935),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color(0xFFEEEEEE)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 공연 정보 섹션
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 공연 상태
                    if (!detail.state.isNullOrEmpty()) {
                        InfoRow(label = "공연 상태", value = detail.state)
                    }

                    // 공연 기간
                    val period = buildPeriodString(detail.startDate, detail.endDate)
                    if (period.isNotEmpty()) {
                        InfoRow(label = "공연 기간", value = period)
                    }

                    // 공연 시간 안내
                    if (!detail.dateGuidance.isNullOrEmpty()) {
                        InfoRow(label = "공연 시간", value = detail.dateGuidance)
                    }

                    // 공연장
                    if (!detail.facilityName.isNullOrEmpty()) {
                        InfoRow(label = "공연장", value = detail.facilityName)
                    }

                    // 지역
                    if (!detail.area.isNullOrEmpty()) {
                        InfoRow(label = "지역", value = detail.area)
                    }

                    // 장르
                    if (!detail.genre.isNullOrEmpty()) {
                        InfoRow(label = "장르", value = detail.genre)
                    }

                    // 러닝타임
                    if (!detail.runtime.isNullOrEmpty()) {
                        InfoRow(label = "러닝타임", value = detail.runtime)
                    }

                    // 관람등급
                    if (!detail.ageLimit.isNullOrEmpty()) {
                        InfoRow(label = "관람등급", value = detail.ageLimit)
                    }

                    // 출연진
                    if (!detail.cast.isNullOrEmpty()) {
                        InfoRow(label = "출연진", value = detail.cast)
                    }

                    // 제작진
                    if (!detail.crew.isNullOrEmpty()) {
                        InfoRow(label = "제작진", value = detail.crew)
                    }

                    // 주최
                    if (!detail.hostCompany.isNullOrEmpty()) {
                        InfoRow(label = "주최", value = detail.hostCompany)
                    }

                    // 주관
                    if (!detail.sponsorCompany.isNullOrEmpty()) {
                        InfoRow(label = "주관", value = detail.sponsorCompany)
                    }

                    // 아동 관람 가능 여부
                    if (!detail.child.isNullOrEmpty()) {
                        InfoRow(label = "아동 관람", value = detail.child)
                    }
                }

                // 공연 설명
                if (!detail.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 8.dp,
                        color = Color(0xFFF5F5F5)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "공연 소개",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = detail.description ?: "",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // 예매처 정보
                if (!detail.ticketSiteList.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 8.dp,
                        color = Color(0xFFF5F5F5)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "예매처",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    detail.ticketSiteList?.forEach { site ->
                        if (!site.name.isNullOrEmpty()) {
                            Text(
                                text = "• ${site.name}",
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // 상세 이미지 리스트
                if (!detail.imageUrlList.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 8.dp,
                        color = Color(0xFFF5F5F5)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "상세 이미지",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 이미지 리스트를 vertical하게 배치
                    detail.imageUrlList?.forEach { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "상세 이미지",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                // 하단 여백 (플로팅 버튼과의 간격)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999),
            modifier = Modifier.size(80.dp, 20.dp)
        )
        if (!value.isNullOrEmpty()) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
        }

    }
}

private fun buildPeriodString(startDate: String?, endDate: String?): String {
    return when {
        !startDate.isNullOrEmpty() && !endDate.isNullOrEmpty() -> "$startDate ~ $endDate"
        !startDate.isNullOrEmpty() -> startDate
        !endDate.isNullOrEmpty() -> endDate
        else -> ""
    }
}

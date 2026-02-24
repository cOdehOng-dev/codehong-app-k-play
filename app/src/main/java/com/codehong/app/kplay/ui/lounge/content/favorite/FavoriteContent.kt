package com.codehong.app.kplay.ui.lounge.content.favorite

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.util.toPeriod
import com.codehong.library.widget.R
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FavoriteContent(
    favoriteList: List<FavoritePerformance>,
    isDarkMode: Boolean,
    onItemClick: (String) -> Unit,
    onItemDelete: (String) -> Unit
) {
    if (favoriteList.isEmpty()) {
        EmptyContent()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .hongBackground(if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
        ) {
            items(
                items = favoriteList,
                key = { it.id }
            ) { item ->
                SwipeToDeleteItem(
                    onDelete = { onItemDelete(item.id) }
                ) {
                    FavoritePerformanceItem(
                        item = item,
                        isDarkMode = isDarkMode,
                        onClick = { onItemClick(item.id) }
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = if (isDarkMode) HongColor.DARK_GRAY_100.toColor() else HongColor.GRAY_10.toColor()
                )
            }
        }
    }
}

// TODO 위젯으로 만들어보기
@Composable
private fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val deleteButtonWidthDp = 80.dp
    val density = LocalDensity.current
    val deleteButtonWidthPx = with(density) { deleteButtonWidthDp.toPx() }

    // onSizeChanged로 컨테이너 실제 너비 측정 (BoxWithConstraints 대신)
    var containerWidthPx by remember { mutableStateOf(0f) }

    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // containerWidthPx > 0 가드: 측정 전 isPastThreshold 오작동 방지
    val isPastThreshold by remember {
        derivedStateOf { containerWidthPx > 0f && offsetX.value <= -(containerWidthPx * 0.7f) }
    }

    val draggableState = rememberDraggableState { delta ->
        coroutineScope.launch {
            if (containerWidthPx <= 0f) return@launch
            val newRaw = offsetX.value + delta
            // 85% 지점까지 자유 드래그, 이후 rubber-band
            val newValue = if (newRaw < -containerWidthPx * 0.85f) {
                val overscroll = newRaw + containerWidthPx * 0.85f
                (-containerWidthPx * 0.85f + overscroll * 0.3f)
                    .coerceAtLeast(-containerWidthPx)
            } else {
                newRaw.coerceIn(-containerWidthPx, 0f)
            }
            offsetX.snapTo(newValue)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .onSizeChanged { size -> containerWidthPx = size.width.toFloat() }
    ) {
        // 삭제 영역: 컨테이너 전체를 채우고, 콘텐츠가 밀릴수록 같이 늘어나는 효과
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HongColor.RED_100.toColor())
                .clickable {
                    coroutineScope.launch {
                        offsetX.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                    }
                    onDelete()
                }
        ) {
            // offset { } 은 placement 단계에서 실행되므로 offsetX.value 를 읽어도 recomposition 없이 re-layout만 발생
            // 버튼 중심 = 오른쪽 끝에서 (노출 너비 / 2) → 항상 노출 영역 중앙 유지
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset {
                        val buttonHalfWidthPx = 25.dp.toPx()
                        // CenterEnd 기준에서 왼쪽으로 이동: (노출영역 중심 - 버튼 반폭) 만큼
                        IntOffset(
                            x = (buttonHalfWidthPx + offsetX.value / 2).roundToInt(),
                            y = 0
                        )
                    }
                    .width(50.dp)
                    .height(50.dp)
                    // 풀스와이프 임계값 초과 시 색상 반전으로 "놓으면 삭제" 피드백
                    .hongBackground(
                        color = HongColor.RED_100,
                        radius = HongRadiusInfo(10)
                    ),
                contentAlignment = Alignment.Center
            ) {
                HongTextCompose(
                    HongTextBuilder()
                        .text("삭제")
                        .typography(HongTypo.BODY_14_B)
                        .color(HongColor.WHITE_100)
                        .applyOption()
                )
            }
        }

        // 콘텐츠 (전경) - 밀릴수록 빨간 영역이 늘어나 보임
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        coroutineScope.launch {
                            val fullSwipeThreshold = containerWidthPx * 0.7f
                            when {
                                // 풀스와이프: 컨테이너 70% 초과 OR 빠른 스와이프 → 화면 끝까지 슬라이드 후 삭제
                                offsetX.value <= -fullSwipeThreshold ||
                                (velocity < -1000f && offsetX.value < -deleteButtonWidthPx * 0.5f) -> {
                                    offsetX.animateTo(
                                        -containerWidthPx,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessHigh
                                        )
                                    )
                                    onDelete()
                                }
                                // 절반 이상 당겼으면 삭제 버튼 노출 유지
                                offsetX.value < -deleteButtonWidthPx / 2f -> {
                                    offsetX.animateTo(
                                        -deleteButtonWidthPx,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                                // 그 외: 원위치
                                else -> {
                                    offsetX.animateTo(
                                        0f,
                                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                                    )
                                }
                            }
                        }
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
private fun FavoritePerformanceItem(
    item: FavoritePerformance,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hongBackground(if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 포스터 이미지
        HongImageCompose(
            option = HongImageBuilder()
                .width(60)
                .height(80)
                .imageInfo(item.posterUrl)
                .radius(HongRadiusInfo(all = 8))
                .scaleType(HongScaleType.CENTER_CROP)
                .applyOption()
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 공연 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 공연명
            HongTextCompose(
                option = HongTextBuilder()
                    .text(item.name ?: "")
                    .typography(HongTypo.BODY_16_B)
                    .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                    .applyOption()
            )

            if (!item.facilityName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                HongTextCompose(
                    option = HongTextBuilder()
                        .text(item.facilityName)
                        .typography(HongTypo.BODY_13)
                        .color(HongColor.GRAY_50)
                        .applyOption()
                )
            }

            val period = Pair(item.startDate, item.endDate).toPeriod()
            if (period.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                HongTextCompose(
                    option = HongTextBuilder()
                        .text(period)
                        .typography(HongTypo.BODY_13)
                        .color(HongColor.GRAY_50)
                        .applyOption()
                )
            }
        }

        // 화살표 아이콘
        HongImageCompose(
            option = HongImageBuilder()
                .width(20)
                .height(20)
                .imageInfo(R.drawable.honglib_ic_arrow_right)
                .imageColor(HongColor.GRAY_50)
                .scaleType(HongScaleType.CENTER_CROP)
                .applyOption()
        )
    }
}

// ===== 빈 탭 공통 콘텐츠 =====
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "찜한 공연이 없어요:(",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HongColor.DARK_GRAY_100.toColor()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "관심있는 공연을 찜해보세요",
                fontSize = 14.sp,
                color = HongColor.GRAY_50.toColor(),
                textAlign = TextAlign.Center
            )
        }
    }
}
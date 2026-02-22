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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val deleteButtonWidthPx = with(LocalDensity.current) { deleteButtonWidthDp.toPx() }
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val draggableState = rememberDraggableState { delta ->
        coroutineScope.launch {
            offsetX.snapTo((offsetX.value + delta).coerceIn(-deleteButtonWidthPx, 0f))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // 삭제 버튼 (배경)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(deleteButtonWidthDp)
                .fillMaxHeight()
                .background(Color.Red)
                .clickable {
                    coroutineScope.launch {
                        offsetX.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                    }
                    onDelete()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "삭제",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 콘텐츠 (전경)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        coroutineScope.launch {
                            val targetOffset = if (offsetX.value < -deleteButtonWidthPx / 2) {
                                -deleteButtonWidthPx
                            } else {
                                0f
                            }
                            offsetX.animateTo(
                                targetOffset,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium)
                            )
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
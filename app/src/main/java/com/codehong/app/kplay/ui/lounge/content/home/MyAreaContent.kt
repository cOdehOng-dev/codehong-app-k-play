package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.ui.lounge.content.RowShimmer
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.disableRippleClickable
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import kotlinx.coroutines.launch

@Composable
fun MyAreaContent(
    isLoading: Boolean,
    currentMonth: Int,
    myAreaList: List<PerformanceInfoItem>,
    onClickRefresh: () -> Unit,
    onClickProduct: (PerformanceInfoItem) -> Unit
) {
    val rotationAngle = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HongTextCompose(
            option = HongTextBuilder()
                .text("${currentMonth}월 내 주변 공연이에요")
                .typography(HongTypo.BODY_20_B)
                .color(HongColor.BLACK_100)
                .applyOption()
        )
        Spacer(modifier = Modifier.width(3.dp))
        Box(
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
                .graphicsLayer {
                    rotationZ = rotationAngle.value
                }
                .disableRippleClickable {
                    onClickRefresh()
                    coroutineScope.launch {
                        rotationAngle.animateTo(
                            targetValue = 360f,
                            animationSpec = tween(durationMillis = 1000)
                        )
                        rotationAngle.snapTo(0f)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            HongImageCompose(
                HongImageBuilder()
                    .width(18)
                    .height(18)
                    .imageInfo(R.drawable.honglib_ic_refresh)
                    .applyOption()
            )
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    if (isLoading) {
        RowShimmer()
        return
    }

    if (myAreaList.isEmpty()) {
        EmptyContent("주변 공연 정보가 없어요")
    } else {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            myAreaList.forEach { item ->
                PerformanceInfoContent(
                    item = item,
                    onClick = { onClickProduct(item) }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(17.dp))
}
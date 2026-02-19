package com.codehong.app.kplay.ui.performance.detail.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun PerformanceDetailNoticeContent(
    imageUrlList: List<String>?
) {
    if (imageUrlList.isNullOrEmpty()) return

    HorizontalDivider(
        thickness = 8.dp,
        color = HongColor.GRAY_10.toColor()
    )

    Spacer(modifier = Modifier.height(16.dp))

    HongTextCompose(
        option = HongTextBuilder()
            .padding(HongSpacingInfo(left  = 16f, right = 16f))
            .text("공지사항")
            .typography(HongTypo.BODY_18_B)
            .color(HongColor.BLACK_100)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(26.dp))

    // 이미지 리스트를 vertical하게 배치
    imageUrlList.forEach { imageUrl ->
        HongImageCompose(
            HongImageBuilder()
                .width(HongLayoutParam.MATCH_PARENT.value)
                .imageInfo(imageUrl)
                .scaleType(HongScaleType.FIT_WIDTH)
                .applyOption()
        )
    }
}
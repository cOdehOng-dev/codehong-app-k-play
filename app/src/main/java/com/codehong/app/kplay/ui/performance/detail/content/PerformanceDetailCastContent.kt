package com.codehong.app.kplay.ui.performance.detail.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun PerformanceDetailCastContent(
    castInfo: String?,
    crewInfo: String?,
    isDarkMode: Boolean = false
) {
    if (castInfo.isNullOrBlank() && crewInfo.isNullOrBlank()) return

    HorizontalDivider(
        thickness = 8.dp,
        color = (if (isDarkMode) HongColor.DARK_GRAY_100 else HongColor.GRAY_10).toColor()
    )

    Spacer(modifier = Modifier.height(16.dp))

    HongTextCompose(
        option = HongTextBuilder()
            .padding(HongSpacingInfo(left  = 16f, right = 16f))
            .text("출연진 및 제작진")
            .typography(HongTypo.BODY_18_B)
            .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(26.dp))

    if (!castInfo.isNullOrBlank()) {
        HongTextCompose(
            option = HongTextBuilder()
                .padding(HongSpacingInfo(left  = 16f, right = 16f, bottom = 10f))
                .text(castInfo)
                .typography(HongTypo.BODY_16)
                .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                .applyOption()
        )
    }

    if (!crewInfo.isNullOrBlank()) {
        HongTextCompose(
            option = HongTextBuilder()
                .padding(HongSpacingInfo(left  = 16f, right = 16f, bottom = 16f))
                .text(crewInfo)
                .typography(HongTypo.BODY_16)
                .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                .applyOption()
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}
package com.codehong.app.kplay.ui.common

import androidx.compose.runtime.Composable
import com.codehong.library.widget.rule.HongBorderInfo
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.badge.HongTextBadgeBuilder
import com.codehong.library.widget.text.badge.HongTextBadgeCompose

@Composable
fun Badge(
    text: String?
) {
    if (text.isNullOrEmpty()) return

    HongTextBadgeCompose(
        option = HongTextBadgeBuilder()
            .text(text)
            .textColor(HongColor.MAIN_ORANGE_70)
            .border(HongBorderInfo(
                width = 1,
                color = HongColor.MAIN_ORANGE_100.hex
            ))
            .textTypo(HongTypo.CONTENTS_10_B)
            .backgroundColor(HongColor.WHITE_100)
            .radius(HongRadiusInfo(all = 4))
            .padding(HongSpacingInfo(left = 6f, right = 6f, top = 4f, bottom = 4f))
            .applyOption()
    )
}
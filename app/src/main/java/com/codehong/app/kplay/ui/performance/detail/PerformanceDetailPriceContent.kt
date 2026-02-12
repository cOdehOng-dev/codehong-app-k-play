package com.codehong.app.kplay.ui.performance.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongSpacing
import com.codehong.library.widget.extensions.splitAtFirstDigit
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun PerformanceDetailPriceContent(
    priceInfo: String?
) {
    if (priceInfo.isNullOrEmpty()) return
    HorizontalDivider(
        thickness = 8.dp,
        color = HongColor.GRAY_10.toColor()
    )

    Spacer(modifier = Modifier.height(16.dp))

    HongTextCompose(
        option = HongTextBuilder()
            .padding(HongSpacingInfo(left  = 16f, right = 16f))
            .text("가격")
            .typography(HongTypo.BODY_18_B)
            .color(HongColor.BLACK_100)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(10.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .hongSpacing(HongSpacingInfo(16f, 16f, 16f, 16f))
            .hongBackground(color = HongColor.GRAY_05, radius = HongRadiusInfo(10))
            .hongSpacing(HongSpacingInfo(20f, 20f, 20f, 20f))
    ) {
        priceInfo.split(", ").forEachIndexed { i, price ->

            if (i > 0) {
                Spacer(modifier = Modifier.height(20.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                price.splitAtFirstDigit().let { info ->
                    HongTextCompose(
                        option = HongTextBuilder()
                            .text(info.first)
                            .typography(HongTypo.BODY_16)
                            .color(HongColor.BLACK_100)
                            .applyOption()
                    )
                    HongTextCompose(
                        option = HongTextBuilder()
                            .text(info.second)
                            .typography(HongTypo.BODY_16_B)
                            .color(HongColor.BLACK_100)
                            .applyOption()
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}
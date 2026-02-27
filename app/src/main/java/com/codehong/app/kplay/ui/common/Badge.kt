package com.codehong.app.kplay.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.rule.HongBorderInfo
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.badge.HongTextBadgeBuilder
import com.codehong.library.widget.text.badge.HongTextBadgeCompose
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun Badge(text: String?) {
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AwardBadgeList(awards: String?) {
    if (awards.isNullOrBlank()) return

    // <br> 또는 <br/> 또는 <br /> 태그로 분리
    val awardList = awards
        .split(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE))
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .take(2)

    if (awardList.isEmpty()) return

    // FlowRow: 공간이 부족하면 자동으로 다음 줄로 이동
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        awardList.forEach { award ->
            Box(
                modifier = Modifier
                    .hongBackground(
                        color = HongColor.GRAY_05,
                        radius = HongRadiusInfo(4)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                HongTextCompose(
                    option = HongTextBuilder()
                        .text(award)
                        .typography(HongTypo.CONTENTS_10_B)
                        .color(HongColor.BLACK_50)
                        .applyOption()
                )
            }
        }
    }
}
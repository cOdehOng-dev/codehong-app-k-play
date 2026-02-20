package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.ui.common.Badge
import com.codehong.library.widget.extensions.clickPress
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun RankPerformanceInfoContent(
    item: BoxOfficeItem,
    rank: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
            .clickPress {
                onClick()
            }
    ) {
        Box {
            HongImageCompose(
                HongImageBuilder()
                    .width(130)
                    .height(170)
                    .radius(HongRadiusInfo(8))
                    .imageInfo(item.posterUrl)
                    .scaleType(HongScaleType.CENTER_CROP)
                    .applyOption()
            )

            // 순위 배지 (좌측 하단)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                HongTextCompose(
                    option = HongTextBuilder()
                        .text("$rank")
                        .typography(HongTypo.TITLE_36_B)
                        .color(HongColor.WHITE_100)
                        .maxLines(1)
                        .applyOption()
                )
            }
        }

        // 공연명
        HongTextCompose(
            option = HongTextBuilder()
                .margin(HongSpacingInfo(top = 8f))
                .text(item.performanceName)
                .typography(HongTypo.BODY_14_B)
                .color(HongColor.BLACK_100)
                .maxLines(2)
                .overflow(HongTextOverflow.ELLIPSIS)
                .applyOption()
        )

        // 장소명
        HongTextCompose(
            option = HongTextBuilder()
                .margin(HongSpacingInfo(top = 8f))
                .text(item.placeName)
                .typography(HongTypo.CONTENTS_12)
                .color(HongColor.GRAY_50)
                .maxLines(1)
                .overflow(HongTextOverflow.ELLIPSIS)
                .applyOption()
        )

        // 공연 기간
        if (!item.performancePeriod.isNullOrEmpty()) {
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(top = 2f))
                    .text(item.performancePeriod)
                    .typography(HongTypo.CONTENTS_10)
                    .color(HongColor.GRAY_40)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 뱃지 (장르, 지역)
        Badge(item.area)
    }
}
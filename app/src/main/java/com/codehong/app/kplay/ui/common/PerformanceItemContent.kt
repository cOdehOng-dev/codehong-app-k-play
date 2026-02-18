package com.codehong.app.kplay.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
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
import com.codehong.library.widget.util.HongDateUtil.formatPerformancePeriod

@Composable
fun PerformanceItemContent(
    item: PerformanceInfoItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HongImageCompose(
            HongImageBuilder()
                .width(100)
                .height(130)
                .radius(HongRadiusInfo(8))
                .imageInfo(item.posterUrl)
                .scaleType(HongScaleType.CENTER_CROP)
                .applyOption()
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 정보 영역
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            // 공연명
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 8f))
                    .text(item.name)
                    .typography(HongTypo.BODY_15_B)
                    .color(HongColor.BLACK_100)
                    .maxLines(2)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            // 장소명
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 6f))
                    .text(item.placeName)
                    .typography(HongTypo.BODY_13)
                    .color(HongColor.DARK_GRAY_100)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            // 공연 기간
            HongTextCompose(
                option = HongTextBuilder()
                    .margin(HongSpacingInfo(bottom = 6f))
                    .text(Pair(item.startDate, item.endDate).formatPerformancePeriod())
                    .typography(HongTypo.CONTENTS_12)
                    .color(HongColor.GRAY_50)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 장르, 지역 뱃지
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item.area
                    .takeIf { !it.isNullOrBlank() }
                    ?.let {
                        Badge(text = it)
                    }
                item.genre
                    .takeIf { !it.isNullOrBlank() }
                    ?.let {
                        Badge(text = it)
                    }
            }
        }
    }
}
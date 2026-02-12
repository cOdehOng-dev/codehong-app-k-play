package com.codehong.app.kplay.ui.lounge.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongSpacing
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun LoungeHeaderContent(
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .hongBackground(HongColor.WHITE_100)
            .hongSpacing(HongSpacingInfo(left = 12f, right = 12f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 검색바 (헤더 내)
        Row(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .hongBackground(
                    color = HongColor.GRAY_05,
                    radius = HongRadiusInfo(8)
                )
                .clickable(onClick = onSearchClick)
                .hongSpacing(HongSpacingInfo(left = 12f, right = 12f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HongImageCompose(
                HongImageBuilder()
                    .width(25)
                    .height(25)
                    .imageInfo(R.drawable.honglib_ic_search)
                    .imageColor(HongColor.MAIN_ORANGE_100)
                    .applyOption()
            )
            Spacer(modifier = Modifier.width(8.dp))
            HongTextCompose(
                option = HongTextBuilder()
                    .width(HongLayoutParam.MATCH_PARENT.value)
                    .text("찾고 싶은 공연, 배우를 검색해보세요")
                    .typography(HongTypo.CONTENTS_14)
                    .color(HongColor.GRAY_50)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )
        }
    }
}

// TODO HONG box 제거 후 image에 clickable 적용
@Composable
fun ArrowRightIcon(
    onMoreClick: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onMoreClick),
        contentAlignment = Alignment.Center
    ) {
        if (isPreview) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.honglib_ic_arrow_right),
                contentDescription = null
            )
        } else {
            HongImageCompose(
                HongImageBuilder()
                    .width(25)
                    .height(25)
                    .imageInfo(R.drawable.honglib_ic_arrow_right)
                    .applyOption()
            )
        }
    }
}

@Composable
fun EmptyContent(
    emptyText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        HongTextCompose(
            option = HongTextBuilder()
                .text(emptyText)
                .typography(HongTypo.BODY_20_B)
                .color(HongColor.DARK_GRAY_100)
                .applyOption()
        )
    }
}

@Composable
fun ArrowTitleContent(
    title: String,
    onClickMore: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hongSpacing(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HongTextCompose(
            option = HongTextBuilder()
                .text(title)
                .typography(HongTypo.BODY_20_B)
                .color(HongColor.BLACK_100)
                .applyOption()
        )

        ArrowRightIcon(onClickMore)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoungeHeaderContentPreview() {
    LoungeHeaderContent(
        onSearchClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LoungeArrowRightIconPreview() {
    ArrowRightIcon(
        onMoreClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LoungeEmptyContentPreview() {
    EmptyContent(
        emptyText = "데이터가 없습니다"
    )
}

@Preview(showBackground = true)
@Composable
private fun LoungeArrowTitleContentPreview() {
    ArrowTitleContent(
        title = "추천 공연이에요",
        onClickMore = {}
    )
}
package com.codehong.app.kplay.ui.lounge.content.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.util.toPeriod
import com.codehong.library.widget.R
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.header.icon.HongHeaderIcon
import com.codehong.library.widget.header.icon.HongHeaderIconBuilder
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.swipe.HongSwipeContainer
import com.codehong.library.widget.swipe.HongSwipeContainerBuilder
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

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
            item("header") {
                HongHeaderIcon(
                    HongHeaderIconBuilder()
                        .title("찜")
                        .titleTypo(HongTypo.BODY_16_B)
                        .titleColor(HongColor.BLACK_100.hex)
                        .applyOption()
                )
            }

            items(
                items = favoriteList,
                key = { it.id }
            ) { item ->
                HongSwipeContainer(
                    HongSwipeContainerBuilder()
                        .buttonText("삭제")
                        .onClickButton {
                            onItemDelete(item.id)
                        }
                        .onClick {
                            onItemClick(item.id)
                        }
                        .content {
                            FavoritePerformanceItem(
                                item = item,
                                isDarkMode = isDarkMode,
                                onClick = { onItemClick(item.id) }
                            )
                        }
                        .applyOption()
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = if (isDarkMode) HongColor.DARK_GRAY_100.toColor() else HongColor.GRAY_10.toColor()
                )
            }
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

        Column(
            modifier = Modifier.weight(1f)
        ) {
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

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .hongBackground(HongColor.TRANSPARENT),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HongTextCompose(
                option = HongTextBuilder()
                    .text("찜한 공연이 없어요:(")
                    .margin(HongSpacingInfo(bottom = 12f))
                    .typography(HongTypo.TITLE_24_B)
                    .color(HongColor.DARK_GRAY_100)
                    .applyOption()
            )
            HongTextCompose(
                option = HongTextBuilder()
                    .text("관심있는 공연을 찜해보세요")
                    .typography(HongTypo.BODY_14)
                    .color(HongColor.GRAY_50)
                    .applyOption()
            )
        }
    }
}
package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.size.Size
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongSpacing
import com.codehong.library.widget.extensions.shimmerEffect
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.pager.HongHorizontalPagerBuilder
import com.codehong.library.widget.pager.HongHorizontalPagerCompose
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun TopBannerContent(
    isLoading: Boolean,
    bannerList: List<BoxOfficeItem>,
    onBannerClick: (BoxOfficeItem) -> Unit
) {
    val actualItemCount = bannerList.size
    var currentIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .hongBackground(HongColor.WHITE_100)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
            return
        }

        if (bannerList.isEmpty()) return

        HongHorizontalPagerCompose(
            HongHorizontalPagerBuilder()
                .width(HongLayoutParam.MATCH_PARENT.value)
                .height(HongLayoutParam.MATCH_PARENT.value)
                .autoScrollMillSecond(2000)
                .infiniteScroll(true, false)
                .backgroundColor(HongColor.WHITE_100.hex)
                .pageInfoList(bannerList)
                .currentIndex { page ->
                    TimberUtil.d("기본 현재 인덱스: $currentIndex")
                    currentIndex = page % actualItemCount
                }
                .applyOption()
        ) { item ->
            (item as? BoxOfficeItem)?.let {
                BannerContent(
                    item = it,
                    onClick = { onBannerClick(it) }
                )
            }
        }

        // 인디케이터 바 (하단) - 프로그레스 바 스타일
        val progress = (currentIndex + 1).toFloat() / actualItemCount.toFloat()

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .hongSpacing(
                    HongSpacingInfo(
                        left = 16f,
                        right = 16f,
                        top = 12f,
                        bottom = 12f
                    )
                )
        ) {
            // 배경 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .hongBackground(
                        color = HongColor.WHITE_30,
                        radius = HongRadiusInfo(1)
                    )
            )
            // 진행 바
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(3.dp)
                    .hongBackground(
                        color = HongColor.WHITE_100,
                        radius = HongRadiusInfo(1)
                    )
            )
        }
    }
}

@Composable
private fun BannerContent(
    item: BoxOfficeItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        // 배경 이미지 (고화질, 전체 표시)
        HongImageCompose(
            HongImageBuilder()
                .width(HongLayoutParam.MATCH_PARENT.value)
                .height(HongLayoutParam.MATCH_PARENT.value)
                .imageInfo(item.posterUrl)
                .scaleType(HongScaleType.FIT_XY)
                .size(Size.ORIGINAL)
                .crossFade(true)
                .applyOption()
        )

        // 그라데이션 오버레이 (하단)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 300f
                    )
                )
        )

        // 텍스트 정보 (좌측 하단)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 32.dp, end = 16.dp)
        ) {
            // 공연 이름 (크게)
            HongTextCompose(
                option = HongTextBuilder()
                    .text(item.performanceName)
                    .typography(HongTypo.TITLE_22_B)
                    .color(HongColor.WHITE_100)
                    .maxLines(2)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 공연장
            HongTextCompose(
                option = HongTextBuilder()
                    .text(item.placeName)
                    .typography(HongTypo.BODY_14)
                    .color(HongColor.WHITE_90)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )


            Spacer(modifier = Modifier.height(2.dp))

            // 기간 (startDate ~ endDate)
            HongTextCompose(
                option = HongTextBuilder()
                    .text(item.performancePeriod)
                    .typography(HongTypo.CONTENTS_12)
                    .color(HongColor.WHITE_80)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .applyOption()
            )
        }
    }
}
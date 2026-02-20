package com.codehong.app.kplay.ui.performance.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailCastContent
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailInfoContent
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailNoticeContent
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailPlaceContent
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailPriceContent
import com.codehong.app.kplay.ui.performance.detail.content.PerformanceDetailTimeTableContent
import com.codehong.library.widget.R
import com.codehong.library.widget.button.text.HongButtonTextBuilder
import com.codehong.library.widget.button.text.HongButtonTextCompose
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongSpacing
import com.codehong.library.widget.header.icon.HongHeaderIcon
import com.codehong.library.widget.header.icon.HongHeaderIconBuilder
import com.codehong.library.widget.image.blur.HongImageBlur
import com.codehong.library.widget.image.blur.HongImageBlurBuilder
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.picker.HongPicker
import com.codehong.library.widget.picker.HongPickerBuilder
import com.codehong.library.widget.progress.HongProgress
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongScaleType
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceDetailScreen(
    state: PerformanceDetailState,
    onEvent: (PerformanceDetailEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .hongBackground(if (state.isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100),
        topBar = {
            PerformanceDetailTopBar(
                title = state.performanceDetail?.name,
                isDarkMode = state.isDarkMode,
                onBackClick = { onEvent(PerformanceDetailEvent.OnBackClick) },
                onToggleDarkMode = { onEvent(PerformanceDetailEvent.OnToggleDarkMode) }
            )
        },
        bottomBar = {
            PerformanceDetailBottomBar(
                isDarkMode = state.isDarkMode,
                onBookingClick = { onEvent(PerformanceDetailEvent.OnBookingClick) }
            )
        }
    ) { innerPadding ->
        if (state.loading.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                HongProgress()
            }
        } else if (state.performanceDetail != null) {
            PerformanceDetailBody(
                state = state,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    PerformanceDetailReservationPicker(
        state = state,
        onEvent = onEvent
    )
}

@Composable
private fun PerformanceDetailTopBar(
    title: String?,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        HongHeaderIcon(
            HongHeaderIconBuilder()
                .backgroundColor(if (isDarkMode) HongColor.BLACK_100.hex else HongColor.WHITE_100.hex)
                .titleColor(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                .titleTypo(HongTypo.BODY_17_B)
                .title(title)
                .backIcon(R.drawable.honglib_ic_arrow_left)
                .backIconColor(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                .onBack { onBackClick() }
                .applyOption()
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onToggleDarkMode() }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            HongTextCompose(
                option = HongTextBuilder()
                    .text(if (isDarkMode) "라이트" else "다크")
                    .typography(HongTypo.CONTENTS_14)
                    .color(if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                    .applyOption()
            )
        }
    }
}

@Composable
private fun PerformanceDetailBottomBar(
    isDarkMode: Boolean,
    onBookingClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .hongBackground(if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
    ) {
        HongButtonTextCompose(
            HongButtonTextBuilder()
                .margin(HongSpacingInfo(left = 16f, right = 16f, top = 12f, bottom = 12f))
                .width(HongLayoutParam.MATCH_PARENT.value)
                .height(52)
                .text("예매하기")
                .textTypo(HongTypo.BODY_18_B)
                .textColor(HongColor.WHITE_100)
                .backgroundColor(HongColor.MAIN_ORANGE_100.hex)
                .radius(HongRadiusInfo(all = 12))
                .onClick { onBookingClick() }
                .applyOption()
        )
    }
}

@Composable
private fun PerformanceDetailBody(
    state: PerformanceDetailState,
    modifier: Modifier = Modifier
) {
    val performanceDetail = state.performanceDetail ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .hongBackground(if (state.isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100)
            .verticalScroll(rememberScrollState())
    ) {
        PerformanceDetailPosterSection(posterUrl = performanceDetail.posterUrl)

        HongTextCompose(
            option = HongTextBuilder()
                .margin(HongSpacingInfo(top = 20f))
                .padding(HongSpacingInfo(left = 16f, right = 16f))
                .text(performanceDetail.name)
                .typography(HongTypo.TITLE_22_B)
                .color(if (state.isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100)
                .applyOption()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PerformanceDetailInfoSection(
            period = state.period,
            performanceDetail = performanceDetail,
            isDarkMode = state.isDarkMode
        )

        Spacer(modifier = Modifier.height(16.dp))

        PerformanceDetailCastContent(performanceDetail.cast, performanceDetail.crew, state.isDarkMode)

        PerformanceDetailTimeTableContent(performanceDetail.dateGuidance, state.isDarkMode)

        PerformanceDetailPriceContent(performanceDetail.priceInfo, state.isDarkMode)

        PerformanceDetailPlaceContent(state.placeDetail, state.isDarkMode)

        PerformanceDetailNoticeContent(performanceDetail.imageUrlList)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PerformanceDetailPosterSection(posterUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 3f)
            .clipToBounds()
            .hongSpacing(HongSpacingInfo(left = 16f, right = 16f)),
        contentAlignment = Alignment.Center
    ) {
        HongImageBlur(
            HongImageBlurBuilder()
                .imageInfo(posterUrl)
                .blur(30)
                .radius(HongRadiusInfo(16))
                .applyOption()
        )
        Box(
            modifier = Modifier.fillMaxWidth(0.55f)
        ) {
            HongImageCompose(
                option = HongImageBuilder()
                    .width(HongLayoutParam.MATCH_PARENT.value)
                    .height(HongLayoutParam.MATCH_PARENT.value)
                    .scaleType(HongScaleType.FIT_START)
                    .imageInfo(posterUrl)
                    .radius(HongRadiusInfo(16))
                    .applyOption()
            )
        }
    }
}

@Composable
private fun PerformanceDetailInfoSection(
    period: String,
    performanceDetail: PerformanceDetail,
    isDarkMode: Boolean = false
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        period.takeIf { it.isNotBlank() }?.let {
            PerformanceDetailInfoContent(label = "공연 기간", value = it, isDarkMode = isDarkMode)
        }

        performanceDetail.facilityName?.takeIf { it.isNotBlank() }?.let {
            PerformanceDetailInfoContent(label = "공연장", value = it, isDarkMode = isDarkMode)
        }
        performanceDetail.runtime?.takeIf { it.isNotBlank() && it != "0분" }?.let {
            PerformanceDetailInfoContent(label = "러닝타임", value = it, isDarkMode = isDarkMode)
        }

        performanceDetail.ageLimit?.takeIf { it.isNotBlank() }?.let {
            PerformanceDetailInfoContent(label = "관람등급", value = it, isDarkMode = isDarkMode)
        }

        performanceDetail.hostCompany?.takeIf { it.isNotBlank() }?.let {
            PerformanceDetailInfoContent(label = "주최", value = it, isDarkMode = isDarkMode)
        }

        performanceDetail.sponsorCompany?.takeIf { it.isNotBlank() }?.let {
            PerformanceDetailInfoContent(label = "주관", value = it, isDarkMode = isDarkMode)
        }
    }
}

@Composable
private fun PerformanceDetailReservationPicker(
    state: PerformanceDetailState,
    onEvent: (PerformanceDetailEvent) -> Unit
) {
    if (state.realSiteList.isEmpty()) return

    HongPicker(
        visible = state.isShowReservationPicker,
        option = HongPickerBuilder()
            .title("예매처 선택")
            .firstOptionList(state.siteNameList)
            .buttonText("선택")
            .useDimClickClose(false)
            .selectorColor(HongColor.GRAY_10.hex)
            .onDismiss {
                onEvent(PerformanceDetailEvent.OnHideBookingPicker)
            }
            .onConfirm { selectedFirstOption, _ ->
                val selectUrl = state.realSiteList.firstOrNull { it.name == selectedFirstOption.second }?.url
                onEvent(PerformanceDetailEvent.OnBookingSiteClick(selectUrl))
            }
            .applyOption(),
    )
}

package com.codehong.app.kplay.ui.performance.detail

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
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
    val performanceDetail = state.performanceDetail

    val placeDetail = state.placeDetail

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .hongBackground(HongColor.WHITE_100)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            HongHeaderIcon(
                HongHeaderIconBuilder()
                    .backgroundColor(HongColor.WHITE_100.hex)
                    .titleColor(HongColor.BLACK_100)
                    .titleTypo(HongTypo.BODY_17_B)
                    .title(performanceDetail?.name)
                    .backIcon(R.drawable.honglib_ic_arrow_left)
                    .backIconColor(HongColor.BLACK_100)
                    .onBack { onEvent(PerformanceDetailEvent.OnBackClick) }
                    .applyOption()
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hongBackground(HongColor.WHITE_100)
            ) {
                HongButtonTextCompose(
                    HongButtonTextBuilder()
                        .margin(HongSpacingInfo(
                            left = 16f, right = 16f, top = 12f, bottom = 12f
                        ))
                        .width(HongLayoutParam.MATCH_PARENT.value)
                        .height(52)
                        .text("예매하기")
                        .textTypo(HongTypo.BODY_18_B)
                        .textColor(HongColor.WHITE_100)
                        .backgroundColor(HongColor.MAIN_ORANGE_100.hex)
                        .radius(HongRadiusInfo(all = 12))
                        .onClick {
                            onEvent(PerformanceDetailEvent.OnBookingClick)
                        }
                        .applyOption()
                )
            }
        }
    ) { innerPadding ->
        if (state.loading.isPerformanceDetailLoading) {
            // TODO HONG Shimmer로 전환
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (performanceDetail != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .hongBackground(HongColor.WHITE_100)
                    .verticalScroll(rememberScrollState())
            ) {
                // 상단 포스터 이미지
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 3f)
                        .clipToBounds()
                        .hongSpacing(HongSpacingInfo(left = 16f, right = 16f)),
                    contentAlignment = Alignment.Center
                ) {
                    // 블러 배경 (이미지의 주요 색상을 배경으로 표현)
                    HongImageBlur(
                        HongImageBlurBuilder()
                            .imageInfo(performanceDetail.posterUrl)
                            .blur(30)
                            .radius(HongRadiusInfo(16))
                            .applyOption()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.55f),
                    ) {
                        HongImageCompose(
                            option = HongImageBuilder()
                                .width(HongLayoutParam.MATCH_PARENT.value)
                                .height(HongLayoutParam.MATCH_PARENT.value)
                                .scaleType(HongScaleType.FIT_START)
                                .imageInfo(performanceDetail.posterUrl)
                                .radius(HongRadiusInfo(16))
                                .applyOption()
                        )
                    }
                }

                // 공연명
                HongTextCompose(
                    option = HongTextBuilder()
                        .margin(HongSpacingInfo(top = 20f))
                        .padding(HongSpacingInfo(left  = 16f, right = 16f))
                        .text(performanceDetail.name)
                        .typography(HongTypo.TITLE_22_B)
                        .color(HongColor.BLACK_100)
                        .applyOption()
                )



                Spacer(modifier = Modifier.height(16.dp))

                // 공연 정보 섹션
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // 공연 기간
                    if (state.period.isNotBlank()) {
                        PerformanceDetailInfoContent(label = "공연 기간", value = state.period)
                    }

                    // 공연장
                    performanceDetail.facilityName?.takeIf {
                        it.isNotBlank()
                    }?.let {
                        PerformanceDetailInfoContent(label = "공연장", value = it)
                    }

                    // 러닝타임
                    if (!performanceDetail.runtime.isNullOrBlank()) {
                        PerformanceDetailInfoContent(label = "러닝타임", value = performanceDetail.runtime)
                    }

                    // 관람등급
                    if (!performanceDetail.ageLimit.isNullOrBlank()) {
                        PerformanceDetailInfoContent(label = "관람등급", value = performanceDetail.ageLimit)
                    }

                    // 주최
                    if (!performanceDetail.hostCompany.isNullOrBlank()) {
                        PerformanceDetailInfoContent(label = "주최", value = performanceDetail.hostCompany)
                    }

                    // 주관
                    if (!performanceDetail.sponsorCompany.isNullOrBlank()) {
                        PerformanceDetailInfoContent(label = "주관", value = performanceDetail.sponsorCompany)
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))

                // 출연진
                PerformanceDetailCastContent(performanceDetail.cast, performanceDetail.crew)

                // 시간표 정보
                PerformanceDetailTimeTableContent(performanceDetail.dateGuidance)

                // 가격 정보
                PerformanceDetailPriceContent(performanceDetail.priceInfo)

                // 공연 장소
                PerformanceDetailPlaceContent(placeDetail)

                // 상세 이미지 리스트
                PerformanceDetailNoticeContent(performanceDetail.imageUrlList)


                // 하단 여백 (플로팅 버튼과의 간격)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (state.realSiteList.isNotEmpty()) {
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
}
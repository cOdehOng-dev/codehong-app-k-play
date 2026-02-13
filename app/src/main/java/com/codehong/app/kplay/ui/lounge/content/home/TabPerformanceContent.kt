package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.ui.lounge.content.Shimmer
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.tab.scroll.HongTabScrollBuilder
import com.codehong.library.widget.tab.scroll.HongTabScrollCompose

@Composable
fun TabPerformanceContent(
    title: String,
    emptyText: String,
    isLoading: Boolean,
    tabList: List<SignGuCode>,
    selectedTab: SignGuCode,
    performanceList: List<PerformanceInfoItem>,
    onTabSelected: (SignGuCode) -> Unit,
    onClickProduct: (PerformanceInfoItem) -> Unit,
    onClickMore: () -> Unit
) {
    val selectedIndex = tabList.indexOf(selectedTab).coerceAtLeast(0)

    val scrollState = rememberScrollState()

    LaunchedEffect(selectedTab) {
        scrollState.scrollTo(0)
    }

    ArrowTitleContent(title, onClickMore)

    Spacer(modifier = Modifier.height(12.dp))

    HongTabScrollCompose(
        HongTabScrollBuilder()
            .padding(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            )
            .tabList(tabList)
            .tabTextList(tabList.map { it.displayName })
            .initialSelectIndex(selectedIndex)
            .selectBackgroundColor(HongColor.MAIN_ORANGE_100.hex)
            .radius(
                HongRadiusInfo(
                    topLeft = 100,
                    topRight = 100,
                    bottomLeft = 100,
                    bottomRight = 100
                )
            )
            .onTabClick { _, item ->
                onTabSelected(item as SignGuCode)
            }
            .tabTextHorizontalPadding(12)
            .tabBetweenPadding(6)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(12.dp))

    if (isLoading) {
        Shimmer()
        return
    }

    if (performanceList.isEmpty()) {
        EmptyContent(emptyText)
    } else {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .height(IntrinsicSize.Max)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            performanceList.forEach { item ->
                PerformanceInfoItemContent(
                    item = item,
                    onClick = { onClickProduct(item) }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(17.dp))
}
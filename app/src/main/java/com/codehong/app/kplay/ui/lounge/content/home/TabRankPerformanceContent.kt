package com.codehong.app.kplay.ui.lounge.content.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.type.RankTab
import com.codehong.app.kplay.ui.lounge.content.RowShimmer
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.tab.scroll.HongTabScrollBuilder
import com.codehong.library.widget.tab.scroll.HongTabScrollCompose
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun TabRankPerformanceContent(
    title: String,
    emptyText: String,
    isLoading: Boolean,
    selectedTab: RankTab,
    tabList: List<BoxOfficeItem>,
    onSelectedTab: (RankTab) -> Unit,
    onClickProduct: (BoxOfficeItem) -> Unit,
) {
    val rankTabList = RankTab.entries
    val selectedIndex = rankTabList.indexOf(selectedTab).coerceAtLeast(0)

    val filteredTabList = tabList.filter { item ->
        val rank = item.rank?.toIntOrNull() ?: 0
        rank in selectedTab.startRank..selectedTab.endRank
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(selectedTab) {
        scrollState.scrollTo(0)
    }

    HongTextCompose(
        option = HongTextBuilder()
            .padding(HongSpacingInfo(left = 16f, right = 16f))
            .text(title)
            .typography(HongTypo.BODY_20_B)
            .color(HongColor.BLACK_100)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(12.dp))

    HongTabScrollCompose(
        HongTabScrollBuilder()
            .padding(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            )
            .tabList(rankTabList)
            .tabTextList(rankTabList.map { it.display })
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
                onSelectedTab(item as RankTab)
            }
            .tabTextHorizontalPadding(12)
            .tabBetweenPadding(6)
            .applyOption()
    )

    Spacer(modifier = Modifier.height(12.dp))

    if (isLoading) {
        RowShimmer()
        return
    }



    if (filteredTabList.isEmpty()) {
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
    } else {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .height(IntrinsicSize.Max)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            filteredTabList.forEachIndexed { index, item ->
                RankPerformanceInfoContent(
                    item = item,
                    rank = index + 1,
                    onClick = { onClickProduct(item) }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(17.dp))
}

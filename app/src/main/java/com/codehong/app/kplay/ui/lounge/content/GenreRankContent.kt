package com.codehong.app.kplay.ui.lounge.content

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
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.tab.scroll.HongTabScrollBuilder
import com.codehong.library.widget.tab.scroll.HongTabScrollCompose

@Composable
fun GenreRankContent(
    isLoading: Boolean,
    genreList: List<GenreCode>,
    selectedTab: GenreCode,
    genreRankList: List<BoxOfficeItem>,
    onSelectedTab: (GenreCode) -> Unit,
    onClickProduct: (BoxOfficeItem) -> Unit,
    onClickMore: () -> Unit
) {
    val selectedIndex = genreList.indexOf(selectedTab).coerceAtLeast(0)

    val scrollState = rememberScrollState()

    LaunchedEffect(selectedTab) {
        scrollState.scrollTo(0)
    }

    ArrowTitleContent("장르별 순위에요", onClickMore)

    Spacer(modifier = Modifier.height(12.dp))

    // 장르 탭 (TabLayout)
    HongTabScrollCompose(
        HongTabScrollBuilder()
            .padding(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            )
            .tabList(genreList)
            .tabTextList(genreList.map { it.displayName })
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
                onSelectedTab(item as GenreCode)
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

    // 장르별 랭킹 가로 리스트
    if (genreRankList.isEmpty()) {
        EmptyContent("장르별 랭킹 정보가 없어요")
    } else {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .height(IntrinsicSize.Max)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            genreRankList.take(10).forEachIndexed { index, item ->
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
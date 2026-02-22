package com.codehong.app.kplay.ui.common

import androidx.compose.runtime.Composable
import com.codehong.app.kplay.domain.type.RegionCode
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.tab.scroll.HongTabScrollBuilder
import com.codehong.library.widget.tab.scroll.HongTabScrollCompose

@Composable
fun RegionScrollTab(
    selectedRegionCode: RegionCode,
    onSignGuCodeSelected: (RegionCode) -> Unit
) {
    val selectedIndex = RegionCode.entries.indexOf(selectedRegionCode)

    HongTabScrollCompose(
        HongTabScrollBuilder()
            .padding(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            )
            .tabList(RegionCode.entries)
            .tabTextList(RegionCode.entries.map { it.displayName })
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
//                onSelectedTab(item as GenreCode)
                onSignGuCodeSelected(item as RegionCode)
            }
            .tabTextHorizontalPadding(12)
            .tabBetweenPadding(6)
            .applyOption()
    )

}
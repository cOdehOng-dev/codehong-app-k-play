package com.codehong.app.kplay.ui.common

import androidx.compose.runtime.Composable
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.tab.scroll.HongTabScrollBuilder
import com.codehong.library.widget.tab.scroll.HongTabScrollCompose

@Composable
fun RegionScrollTab(
    selectedSignGuCode: SignGuCode,
    onSignGuCodeSelected: (SignGuCode) -> Unit
) {
    val selectedIndex = SignGuCode.entries.indexOf(selectedSignGuCode)

    HongTabScrollCompose(
        HongTabScrollBuilder()
            .padding(
                HongSpacingInfo(
                    left = 16f,
                    right = 16f
                )
            )
            .tabList(SignGuCode.entries)
            .tabTextList(SignGuCode.entries.map { it.displayName })
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
                onSignGuCodeSelected(item as SignGuCode)
            }
            .tabTextHorizontalPadding(12)
            .tabBetweenPadding(6)
            .applyOption()
    )

}
package com.codehong.app.kplay.ui.common

import androidx.compose.runtime.Composable
import com.codehong.library.widget.R
import com.codehong.library.widget.header.icon.HongHeaderIcon
import com.codehong.library.widget.header.icon.HongHeaderIconBuilder
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.typo.HongTypo

@Composable
fun BackHeader(
    title: String,
    onBackClick: () -> Unit
) {
    HongHeaderIcon(
        HongHeaderIconBuilder()
            .backgroundColor(HongColor.WHITE_100.hex)
            .titleColor(HongColor.BLACK_100)
            .titleTypo(HongTypo.BODY_17_B)
            .title(title)
            .icon(R.drawable.honglib_ic_arrow_left)
            .iconColor(HongColor.BLACK_100)
            .onClickBack { onBackClick() }
            .applyOption()
    )
}
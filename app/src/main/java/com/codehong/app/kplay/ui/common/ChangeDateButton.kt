package com.codehong.app.kplay.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.button.text.HongButtonTextBuilder
import com.codehong.library.widget.button.text.HongButtonTextCompose
import com.codehong.library.widget.rule.HongBorderInfo
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose
import com.codehong.library.widget.util.HongDateUtil.formatDateDisplay

@Composable
fun ChangeDateButton(
    startDate: String,
    endDate: String,
    onDateChangeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 날짜 정보 (MM.dd ~ MM.dd)
        HongTextCompose(
            option = HongTextBuilder()
                .text(Pair(startDate, endDate).formatDateDisplay())
                .typography(HongTypo.BODY_14_B)
                .color(HongColor.BLACK_100)
                .applyOption()
        )

        // 날짜 변경 버튼
        HongButtonTextCompose(
            HongButtonTextBuilder()
                .width(80)
                .height(35)
                .radius(HongRadiusInfo(8))
                .border(HongBorderInfo(
                    1,
                    HongColor.MAIN_ORANGE_100.hex
                ))
                .text("날짜 변경")
                .textTypo(HongTypo.BODY_13_B)
                .textColor(HongColor.MAIN_ORANGE_100)
                .backgroundColor(HongColor.WHITE_100.hex)
                .onClick { onDateChangeClick() }
                .applyOption()
        )
    }
}
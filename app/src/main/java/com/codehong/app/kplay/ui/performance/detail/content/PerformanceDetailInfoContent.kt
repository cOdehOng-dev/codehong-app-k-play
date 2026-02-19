package com.codehong.app.kplay.ui.performance.detail.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun PerformanceDetailInfoContent(
    label: String,
    value: String?
) {
    if (value.isNullOrBlank()) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HongTextCompose(
            option = HongTextBuilder()
                .text(label)
                .typography(HongTypo.CONTENTS_14)
                .color(HongColor.GRAY_50)
                .applyOption()
        )
        HongTextCompose(
            option = HongTextBuilder()
                .text(value)
                .typography(HongTypo.CONTENTS_14)
                .color(HongColor.BLACK_100)
                .applyOption()
        )
    }
}
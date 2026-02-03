package com.codehong.app.kplay.ui.lounge.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.R
import com.codehong.library.widget.image.HongImageBuilder
import com.codehong.library.widget.image.HongImageCompose

@Composable
fun ArrowRightIcon(
    onMoreClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onMoreClick),
        contentAlignment = Alignment.Center
    ) {
        HongImageCompose(
            HongImageBuilder()
                .width(25)
                .height(25)
                .imageInfo(R.drawable.honglib_ic_arrow_right)
                .applyOption()
        )
    }
}


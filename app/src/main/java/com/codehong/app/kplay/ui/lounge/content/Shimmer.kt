package com.codehong.app.kplay.ui.lounge.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.extensions.shimmerEffect


@Composable
fun Shimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp)
    ) {
        ListItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        ListItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        ListItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        ListItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        ListItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        ListItemShimmer()
    }
}
@Composable
fun ListItemShimmer() {
    Column(
        modifier = Modifier
            .width(130.dp)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(170.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .shimmerEffect()
        )
    }
}
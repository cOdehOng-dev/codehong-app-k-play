package com.codehong.app.kplay.ui.lounge.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.extensions.hongHeight
import com.codehong.library.widget.extensions.hongWidth
import com.codehong.library.widget.extensions.shimmerEffect
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.radius.HongRadiusInfo


@Composable
fun RowShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp)
    ) {
        RowItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        RowItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        RowItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        RowItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        RowItemShimmer()
        Spacer(modifier = Modifier.width(12.dp))
        RowItemShimmer()
    }
}

@Composable
fun ColumnShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
        ColumnItemShimmer()
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun RowItemShimmer() {
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

@Composable
fun ColumnItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp, 130.dp)
                .hongBackground(HongColor.TRANSPARENT, radius = HongRadiusInfo(8))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .hongWidth(250)
                    .hongHeight(30)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .hongWidth(150)
                    .hongHeight(30)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .hongWidth(100)
                    .hongHeight(30)
                    .shimmerEffect()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .hongWidth(50)
                        .hongHeight(10)
                        .hongBackground(HongColor.TRANSPARENT, radius = HongRadiusInfo(4))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .hongWidth(50)
                        .hongHeight(10)
                        .hongBackground(HongColor.TRANSPARENT, radius = HongRadiusInfo(4))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}
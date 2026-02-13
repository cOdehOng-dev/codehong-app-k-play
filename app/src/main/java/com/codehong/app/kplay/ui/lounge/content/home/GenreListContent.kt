package com.codehong.app.kplay.ui.lounge.content.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codehong.app.kplay.R
import com.codehong.app.kplay.domain.type.GenreCode
import com.codehong.library.widget.image.def.HongImageBuilder
import com.codehong.library.widget.image.def.HongImageCompose
import com.codehong.library.widget.rule.HongLayoutParam
import com.codehong.library.widget.rule.HongSpacingInfo
import com.codehong.library.widget.rule.HongTextAlign
import com.codehong.library.widget.rule.HongTextOverflow
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun GenreListContent(
    genreList: List<GenreCode>,
    onClickGenre: (GenreCode) -> Unit
) {
    val rows = genreList.chunked(5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rowItems.forEach { cateCode ->
                    Box(modifier = Modifier.weight(1f)) {
                        GenreContent(
                            genreCode = cateCode,
                            onClick = { onClickGenre(cateCode) }
                        )
                    }
                }
                repeat(5 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GenreContent(
    genreCode: GenreCode,
    onClick: () -> Unit
) {
    val iconResId = remember(genreCode) {
        genreCode.toIconResId()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 40.dp),
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        if (iconResId != 0) {
            HongImageCompose(
                HongImageBuilder()
                    .width(50)
                    .height(50)
                    .imageInfo(iconResId)
                    .applyOption()
            )

            HongTextCompose(
                option = HongTextBuilder()
                    .width(HongLayoutParam.MATCH_PARENT.value)
                    .padding(HongSpacingInfo(top = 6f))
                    .text(genreCode.displayName)
                    .typography(HongTypo.CONTENTS_12)
                    .color(HongColor.BLACK_100)
                    .maxLines(1)
                    .overflow(HongTextOverflow.ELLIPSIS)
                    .textAlign(HongTextAlign.CENTER)
                    .applyOption()
            )
        }
    }
}

@DrawableRes
private fun GenreCode.toIconResId(): Int {
    return when (this) {
        GenreCode.THEATER -> R.drawable.ic_play
        GenreCode.MUSICAL -> R.drawable.ic_musical
        GenreCode.CLASSIC -> R.drawable.ic_west_music
        GenreCode.KOREAN_MUSIC -> R.drawable.ic_koeran_music
        GenreCode.POP_MUSIC -> R.drawable.ic_mass_music
        GenreCode.DANCE -> R.drawable.ic_dancing
        GenreCode.POP_DANCE -> R.drawable.ic_mess_dancing
        GenreCode.CIRCUS_MAGIC -> R.drawable.ic_circus
        GenreCode.KID -> R.drawable.ic_kids
        GenreCode.OPEN_RUN -> R.drawable.ic_open_run
    }
}
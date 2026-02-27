package com.codehong.app.kplay.ui.lounge.content.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.library.widget.extensions.hongBackground
import com.codehong.library.widget.header.icon.HongHeaderIcon
import com.codehong.library.widget.header.icon.HongHeaderIconBuilder
import com.codehong.library.widget.rule.color.HongColor
import com.codehong.library.widget.rule.color.HongColor.Companion.toColor
import com.codehong.library.widget.rule.typo.HongTypo
import com.codehong.library.widget.text.def.HongTextBuilder
import com.codehong.library.widget.text.def.HongTextCompose

@Composable
fun SettingContent(
    isDarkMode: Boolean,
    themeType: ThemeType,
    cacheSizeText: String,
    onThemeChanged: (ThemeType) -> Unit,
    onCacheDeleteConfirmed: () -> Unit
) {
    val bgColor = if (isDarkMode) HongColor.BLACK_100 else HongColor.WHITE_100
    val titleColor = if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100
    val dividerColor = if (isDarkMode) HongColor.DARK_GRAY_100 else HongColor.GRAY_10

    var showThemeDialog by remember { mutableStateOf(false) }
    var showCacheDeleteDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        ThemeSelectDialog(
            isDarkMode = isDarkMode,
            currentTheme = themeType,
            onThemeSelected = { selected ->
                onThemeChanged(selected)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showCacheDeleteDialog) {
        CacheDeleteDialog(
            isDarkMode = isDarkMode,
            cacheSizeText = cacheSizeText,
            onConfirm = {
                onCacheDeleteConfirmed()
                showCacheDeleteDialog = false
            },
            onDismiss = { showCacheDeleteDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .hongBackground(bgColor)
    ) {
        HongHeaderIcon(
            HongHeaderIconBuilder()
                .title("설정")
                .titleTypo(HongTypo.BODY_16_B)
                .titleColor(HongColor.BLACK_100.hex)
                .applyOption()
        )

        // 스크롤 가능한 설정 목록
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 테마 설정 아이템
            SettingRow(
                title = "테마",
                value = themeType.displayName,
                titleColor = titleColor,
                onClick = { showThemeDialog = true }
            )

            HorizontalDivider(thickness = 1.dp, color = dividerColor.toColor())

            // 캐시 데이터 삭제 아이템
            CacheDeleteRow(
                cacheSizeText = cacheSizeText,
                titleColor = titleColor,
                onDeleteClick = { showCacheDeleteDialog = true }
            )

            HorizontalDivider(thickness = 1.dp, color = dividerColor.toColor())
        }
    }
}

@Composable
private fun CacheDeleteRow(
    cacheSizeText: String,
    titleColor: HongColor,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 좌측: 타이틀 + 용량 나란히
        HongTextCompose(
            option = HongTextBuilder()
                .text("캐시 데이터 삭제")
                .typography(HongTypo.BODY_16)
                .color(titleColor)
                .applyOption()
        )
        Spacer(modifier = Modifier.width(8.dp))
        HongTextCompose(
            option = HongTextBuilder()
                .text(cacheSizeText.ifEmpty { "계산 중..." })
                .typography(HongTypo.BODY_13)
                .color(HongColor.GRAY_50)
                .applyOption()
        )

        Spacer(modifier = Modifier.weight(1f))

        // 우측: 삭제 버튼
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = HongColor.MAIN_ORANGE_100.toColor(),
            modifier = Modifier.clickable(onClick = onDeleteClick)
        ) {
            Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                HongTextCompose(
                    option = HongTextBuilder()
                        .text("삭제")
                        .typography(HongTypo.BODY_13)
                        .color(HongColor.WHITE_100)
                        .applyOption()
                )
            }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    value: String,
    titleColor: HongColor,
    valueColor: HongColor = HongColor.GRAY_50,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HongTextCompose(
            option = HongTextBuilder()
                .text(title)
                .typography(HongTypo.BODY_16)
                .color(titleColor)
                .applyOption()
        )
        HongTextCompose(
            option = HongTextBuilder()
                .text(value)
                .typography(HongTypo.BODY_14)
                .color(valueColor)
                .applyOption()
        )
    }
}

@Composable
private fun CacheDeleteDialog(
    isDarkMode: Boolean,
    cacheSizeText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val titleColor = if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            HongTextCompose(
                option = HongTextBuilder()
                    .text("캐시 데이터 삭제")
                    .typography(HongTypo.BODY_16_B)
                    .color(titleColor)
                    .applyOption()
            )
        },
        text = {
            Text(
                text = "현재 캐시 크기: $cacheSizeText\n공연장 위치 데이터를 삭제합니다.\n다음 조회 시 다시 다운로드됩니다.",
                color = HongColor.GRAY_50.toColor(),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "삭제",
                    color = HongColor.MAIN_ORANGE_100.toColor(),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    color = HongColor.GRAY_50.toColor()
                )
            }
        }
    )
}

@Composable
private fun ThemeSelectDialog(
    isDarkMode: Boolean,
    currentTheme: ThemeType,
    onThemeSelected: (ThemeType) -> Unit,
    onDismiss: () -> Unit
) {
    val titleColor = if (isDarkMode) HongColor.WHITE_100 else HongColor.BLACK_100

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "테마 선택",
                fontWeight = FontWeight.Bold,
                color = titleColor.toColor()
            )
        },
        text = {
            Column {
                ThemeType.entries.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable { onThemeSelected(type) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == type,
                            onClick = { onThemeSelected(type) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = HongColor.MAIN_ORANGE_100.toColor(),
                                unselectedColor = HongColor.GRAY_50.toColor()
                            )
                        )
                        Text(
                            text = type.displayName,
                            modifier = Modifier.padding(start = 8.dp),
                            color = titleColor.toColor(),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    color = HongColor.GRAY_50.toColor()
                )
            }
        }
    )
}

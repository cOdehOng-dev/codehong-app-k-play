package com.codehong.app.kplay.domain.type

enum class ThemeType(val displayName: String) {
    LIGHT("라이트 모드"),
    DARK("다크 모드"),
    SYSTEM("시스템 설정");

    companion object {
        fun String?.toThemeType(): ThemeType =
            entries.firstOrNull { it.name == this } ?: SYSTEM
    }
}

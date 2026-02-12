package com.codehong.app.kplay.domain.type

enum class GenreCode(
    val code: String,
    val displayName: String
) {
    THEATER("AAAA", "연극"),
    MUSICAL("GGGA", "뮤지컬"),
    CLASSIC("CCCA", "클래식"),
    KOREAN_MUSIC("CCCC", "국악"),
    POP_MUSIC("CCCD", "대중음악"),
    DANCE("BBBC", "무용"),
    POP_DANCE("BBBR", "대중무용"),
    CIRCUS_MAGIC("EEEB", "서커스/마술"),
    KID("KID", "아동"),
    OPEN_RUN("OPEN", "오픈런");

    companion object {
        fun String?.toCode(): GenreCode? {
            return entries.find { it.code == this }
        }
    }
}
package com.codehong.app.kplay.domain.type

enum class CateCode(
    val code: String,
    val displayName: String,
    val iconResName: String
) {
    THEATER("AAAA", "연극", "ic_play"),
    MUSICAL("GGGA", "뮤지컬", "ic_musical"),
    CLASSIC("CCCA", "클래식", "ic_west_music"),
    KOREAN_MUSIC("CCCC", "국악", "ic_koeran_music"),
    POP_MUSIC("CCCD", "대중음악", "ic_mass_music"),
    DANCE("BBBC", "무용", "ic_dancing"),
    POP_DANCE("BBBR", "대중무용", "ic_mess_dancing"),
    CIRCUS_MAGIC("EEEB", "서커스/마술", "ic_circus"),
    KID("KID", "아동", "ic_kids"),
    OPEN_RUN("OPEN", "오픈런", "ic_open_run");

    companion object {
        fun String?.toCode(): CateCode? {
            return entries.find { it.code == this }
        }
    }
}
package com.codehong.app.kplay.ui.localtab

enum class LocalTabType(
    val type: String,
    val title: String,
) {
    GENRE("genre", "장르별 공연"),
    REGION("region", "지역별 공연"),
    FESTIVAL("festival", "지역 축제"),
    AWARD("award", "수상작")
    ;

    companion object {
        fun String?.toLocalTabType(): LocalTabType {
            return entries.find { it.type == this } ?: GENRE
        }
    }
}
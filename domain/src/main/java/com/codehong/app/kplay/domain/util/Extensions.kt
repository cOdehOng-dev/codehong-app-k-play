package com.codehong.app.kplay.domain.util

fun Pair<String?, String?>.toPeriod(): String {
    val (start, end) = this
    return when {
        !start.isNullOrBlank() && !end.isNullOrBlank() -> "$start ~ $end"
        !start.isNullOrBlank() && end.isNullOrBlank() -> "$start ~ "
        start.isNullOrBlank() && !end.isNullOrBlank() -> " ~ $end"
        else -> ""
    }
}

fun String.fullArea(): String {
    val fullName = when {
        this.contains("서울") -> "서울특별시"
        this.contains("부산") -> "부산광역시"
        this.contains("대구") -> "대구광역시"
        this.contains("인천") -> "인천광역시"
        this.contains("광주") -> "광주광역시"
        this.contains("대전") -> "대전광역시"
        this.contains("울산") -> "울산광역시"
        this.contains("세종") -> "세종특별자치시"
        this.contains("경기") -> "경기도"
        this.contains("강원") -> "강원도"
        this.contains("충북") -> "충청북도"
        this.contains("충남") -> "충청남도"
        this.contains("전북") -> "전라북도"
        this.contains("전남") -> "전라남도"
        this.contains("경북") -> "경상북도"
        this.contains("경남") -> "경상남도"
        this.contains("제주") -> "제주특별자치도"
        else -> this
    }
    return "$fullName 공연"
}
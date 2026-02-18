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

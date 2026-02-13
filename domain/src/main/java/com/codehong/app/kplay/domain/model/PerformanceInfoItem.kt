package com.codehong.app.kplay.domain.model

data class PerformanceInfoItem(
    val id: String? = null,
    val name: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val placeName: String? = null,
    val posterUrl: String? = null,
    val area: String? = null,
    val genre: String? = null,
    val openRun: String? = null,
    val state: String? = null,
    val awards: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val period get() = buildString {
        startDate?.let { append(it) }
        if (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) {
            append(" ~ ")
        }
        endDate?.let { append(it) }
    }
}
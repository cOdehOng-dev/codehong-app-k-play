package com.codehong.app.kplay.domain.model.performance

import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem

data class PerformanceGroup(
    val placeName: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val performanceList: List<PerformanceInfoItem> = emptyList()
)
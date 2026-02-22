package com.codehong.app.kplay.domain.model.place

import com.codehong.app.kplay.domain.model.PerformanceInfoItem

data class PlaceGroup(
    val placeName: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val performanceList: List<PerformanceInfoItem> = emptyList()
)
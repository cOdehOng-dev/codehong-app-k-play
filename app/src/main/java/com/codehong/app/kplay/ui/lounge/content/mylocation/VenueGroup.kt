package com.codehong.app.kplay.ui.lounge.content.mylocation

import com.codehong.app.kplay.domain.model.PerformanceInfoItem

data class VenueGroup(
    val placeName: String,
    val lat: Double?,
    val lng: Double?,
    val performances: List<PerformanceInfoItem>
)

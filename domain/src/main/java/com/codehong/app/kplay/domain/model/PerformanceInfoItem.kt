package com.codehong.app.kplay.domain.model

data class PerformanceInfoItem(
    var id: String? = null,
    var name: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var placeName: String? = null,
    var posterUrl: String? = null,
    var area: String? = null,
    var genre: String? = null,
    var openRun: String? = null,
    var state: String? = null
)
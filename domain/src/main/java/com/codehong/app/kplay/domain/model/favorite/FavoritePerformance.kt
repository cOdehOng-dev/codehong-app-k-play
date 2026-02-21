package com.codehong.app.kplay.domain.model.favorite

data class FavoritePerformance(
    val id: String = "",
    val name: String? = null,
    val posterUrl: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val facilityName: String? = null,
    val genre: String? = null,
)
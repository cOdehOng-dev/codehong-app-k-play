package com.codehong.app.kplay.domain.model.place

data class PlaceDetail(
    val placeId: String? = null,
    val placeName: String? = null,
    val placeAddress: String? = null,
    val latitude: String? = null,
    val longitude: String? = null
) {
    val lat: Double?
        get() = latitude?.toDoubleOrNull()

    val lng: Double?
        get() = longitude?.toDoubleOrNull()
}

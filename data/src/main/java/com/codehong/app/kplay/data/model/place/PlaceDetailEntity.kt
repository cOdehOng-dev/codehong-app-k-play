package com.codehong.app.kplay.data.model.place

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codehong.app.kplay.data.room.RoomConst

@Entity(tableName = RoomConst.TABLE_PLACE_DETAIL)
data class PlaceDetailEntity(
    @PrimaryKey val keyword: String,
    val placeId: String? = null,
    val placeName: String? = null,
    val placeAddress: String? = null,
    val latitude: String? = null,
    val longitude: String? = null
)

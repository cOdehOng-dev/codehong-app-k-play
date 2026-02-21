package com.codehong.app.kplay.data.model.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codehong.app.kplay.data.room.RoomConst

@Entity(tableName = RoomConst.TABLE_FAVORITE_PERFORMANCE)
data class FavoritePerformanceEntity(
    @PrimaryKey val id: String = "",
    val name: String? = null,
    val posterUrl: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val facilityName: String? = null,
    val genre: String? = null
)
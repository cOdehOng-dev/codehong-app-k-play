package com.codehong.app.kplay.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codehong.app.kplay.data.model.place.PlaceDetailEntity
import com.codehong.app.kplay.data.room.RoomConst

@Dao
interface PlaceDetailDao {

    @Query("SELECT * FROM ${RoomConst.TABLE_PLACE_DETAIL} WHERE keyword = :keyword LIMIT 1")
    suspend fun getByKeyword(keyword: String): PlaceDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PlaceDetailEntity)

    @Query("""
        SELECT SUM(
            length(keyword) +
            length(coalesce(placeId, '')) +
            length(coalesce(placeName, '')) +
            length(coalesce(placeAddress, '')) +
            length(coalesce(latitude, '')) +
            length(coalesce(longitude, ''))
        ) FROM ${RoomConst.TABLE_PLACE_DETAIL}
    """)
    suspend fun getSizeBytes(): Long?

    @Query("DELETE FROM ${RoomConst.TABLE_PLACE_DETAIL}")
    suspend fun clearAll()
}

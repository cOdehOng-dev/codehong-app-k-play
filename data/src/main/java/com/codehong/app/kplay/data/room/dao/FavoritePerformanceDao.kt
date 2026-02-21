package com.codehong.app.kplay.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codehong.app.kplay.data.model.favorite.FavoritePerformanceEntity
import com.codehong.app.kplay.data.room.RoomConst
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePerformanceDao {

    @Query("SELECT * FROM ${RoomConst.TABLE_FAVORITE_PERFORMANCE} ORDER BY rowid DESC")
    fun getAllFavorites(): Flow<List<FavoritePerformanceEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: FavoritePerformanceEntity)

    @Query("DELETE FROM ${RoomConst.TABLE_FAVORITE_PERFORMANCE} WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM ${RoomConst.TABLE_FAVORITE_PERFORMANCE} WHERE id = :id")
    suspend fun countById(id: String): Int
}
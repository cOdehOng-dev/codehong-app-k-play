package com.codehong.app.kplay.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codehong.app.kplay.data.model.favorite.FavoritePerformanceEntity
import com.codehong.app.kplay.data.model.place.PlaceDetailEntity
import com.codehong.app.kplay.data.room.dao.FavoritePerformanceDao
import com.codehong.app.kplay.data.room.dao.PlaceDetailDao

// TODO: Entity 스키마를 변경할 경우 아래 작업을 순서대로 진행해주세요.
//  1. RoomConst.DATABASE_VERSION 값을 1 증가시킵니다.
//  2. data/room/migration/ 패키지에 MIGRATION_{이전버전}_{신버전} 객체를 생성합니다.
//     예) val MIGRATION_2_3 = object : Migration(2, 3) {
//             override fun migrate(db: SupportSQLiteDatabase) {
//                 db.execSQL("ALTER TABLE place_detail ADD COLUMN newColumn TEXT")
//             }
//         }
//  3. DatabaseModule.kt 의 .addMigrations(...) 에 해당 Migration 객체를 추가합니다.
//  4. entities 목록에 새 Entity 가 추가된 경우 이 어노테이션 목록에도 추가합니다.
@Database(
    entities = [
        FavoritePerformanceEntity::class,
        PlaceDetailEntity::class           // v2: 공연장 위경도 캐싱
    ],
    version = RoomConst.DATABASE_VERSION,
    exportSchema = false
)
abstract class KPlayDatabase : RoomDatabase() {
    abstract fun favoritePerformanceDao(): FavoritePerformanceDao
    abstract fun placeDetailDao(): PlaceDetailDao
}

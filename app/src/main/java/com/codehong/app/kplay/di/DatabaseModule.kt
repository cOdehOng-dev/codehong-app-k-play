package com.codehong.app.kplay.di

import android.content.Context
import androidx.room.Room
import com.codehong.app.kplay.data.room.KPlayDatabase
import com.codehong.app.kplay.data.room.RoomConst
import com.codehong.app.kplay.data.room.dao.FavoritePerformanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KPlayDatabase =
        Room.databaseBuilder(
            context,
            KPlayDatabase::class.java,
            RoomConst.DATABASE_NAME
        )
            // TODO: RoomConst.DATABASE_VERSION 을 올릴 때마다 Migration 을 추가해주세요.
            //       Migration 객체는 data/room/migration/ 패키지에 작성하는 것을 권장합니다.
            //       예) .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            //
            //       ⚠️ fallbackToDestructiveMigration() 은 Migration 이 없을 때 DB 를 초기화합니다.
            //          실제 운영(배포) 전에 반드시 Migration 을 작성하고 이 옵션을 제거해주세요.
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFavoritePerformanceDao(db: KPlayDatabase): FavoritePerformanceDao =
        db.favoritePerformanceDao()
}

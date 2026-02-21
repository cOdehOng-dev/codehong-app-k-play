package com.codehong.app.kplay.data.room

object RoomConst {

    // TODO: Entity 스키마(컬럼 추가/삭제/수정)를 변경할 때마다 이 버전을 1씩 올려야 합니다.
    //       버전을 올린 후에는 반드시 DatabaseModule.kt 에 해당 버전에 대한 Migration 을 추가해주세요.
    //       예) version 1 → 2 변경 시: MIGRATION_1_2 객체를 만들고 .addMigrations(MIGRATION_1_2) 에 추가
    const val DATABASE_VERSION = 1

    const val DATABASE_NAME = "kplay_database"

    const val TABLE_FAVORITE_PERFORMANCE = "favorite_performance"
}
package com.codehong.app.kplay.data.room

object RoomConst {

    // TODO: Entity 스키마(컬럼 추가/삭제/수정)를 변경할 때마다 이 버전을 1씩 올려야 합니다.
    //       버전을 올린 후에는 반드시 DatabaseModule.kt 에 해당 버전에 대한 Migration 을 추가해주세요.
    //       예) version 2 → 3 변경 시: MIGRATION_2_3 객체를 만들고 .addMigrations(MIGRATION_2_3) 에 추가
    const val DATABASE_VERSION = 2

    const val DATABASE_NAME = "kplay_database"

    const val TABLE_FAVORITE_PERFORMANCE = "favorite_performance"

    // v2: PlaceDetail API 결과 캐싱 테이블 추가
    const val TABLE_PLACE_DETAIL = "place_detail"
}
package com.codehong.app.kplay.util

import com.naver.maps.geometry.LatLng
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object Util {

    fun String.centerAreaLatLng(): LatLng {
        return when {
            this.contains("서울") -> LatLng(37.5666805, 126.9784147)
            this.contains("부산") -> LatLng(35.1795543, 129.0756416)
            this.contains("대구") -> LatLng(35.8714354, 128.601445)
            this.contains("인천") -> LatLng(37.4563, 126.7052)
            this.contains("광주") -> LatLng(35.1595, 126.8526)
            this.contains("대전") -> LatLng(36.3504, 127.3845)
            this.contains("울산") -> LatLng(35.5384, 129.3114)
            this.contains("세종") -> LatLng(36.4800, 127.0000)
            this.contains("경기") -> LatLng(37.4138, 127.5183)
            this.contains("강원") -> LatLng(37.8228, 128.1555)
            this.contains("충북") -> LatLng(36.6357, 127.4912)
            this.contains("충남") -> LatLng(36.5184, 126.8000)
            this.contains("전북") -> LatLng(35.8468, 127.1297)
            this.contains("전남") -> LatLng(34.8679, 126.9910)
            this.contains("경북") -> LatLng(36.4919, 128.8889)
            this.contains("경남") -> LatLng(35.4606, 128.2132)
            this.contains("제주") -> LatLng(33.4996, 126.5312)
            else -> LatLng(37.5666805, 126.9784147)
        }
    }

    fun generateOffset(hashCode: Int, index: Int, isLat: Boolean): Double {
        val seed = if (isLat) hashCode else hashCode * 31 + index
        val angle = (index * 137.5 + abs(seed) % 360) * Math.PI / 180
        val distance = 0.008 + (abs(seed) % 100) * 0.0004
        return if (isLat) distance * cos(angle) else distance * sin(angle)
    }
}
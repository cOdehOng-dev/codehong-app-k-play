package com.codehong.app.kplay.domain.type

enum class RankTab(
    val display: String,
    val startRank: Int,
    val endRank: Int
) {
    TOP_1_10("1~10위", 1, 10),
    TOP_11_20("11~20위", 11, 20),
    TOP_21_30("21~30위", 21, 30),
    TOP_31_40("31~40위", 31, 40),
    TOP_41_50("41~50위", 41, 50)
}
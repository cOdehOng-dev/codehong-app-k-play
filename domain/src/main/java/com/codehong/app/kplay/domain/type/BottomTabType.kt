package com.codehong.app.kplay.domain.type

enum class BottomTabType(val id: String, val label: String) {

    HOME("home", "홈"),
    MY_LOCATION("myLocation", "내주변"),
    BOOKMARK("bookmark", "찜");

    companion object {
        fun String?.toBottomTabType(): BottomTabType {
            if (this.isNullOrBlank()) return HOME
            return entries.firstOrNull { it.id == this } ?: HOME
        }
    }
}
package com.codehong.app.kplay.domain.extensions

fun String?.extractParenthesesContent(): String {
    if (this.isNullOrEmpty()) return ""
    val startIndex = indexOf('(')
    val endIndex = indexOf(')')
    return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        substring(startIndex + 1, endIndex)
    } else {
        this
    }
}
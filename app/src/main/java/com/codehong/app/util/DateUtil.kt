package com.codehong.app.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {

    private val koreaZoneId = ZoneId.of("Asia/Seoul")
    private val yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    fun getCurrentMonth(): Int {
        return LocalDate.now(koreaZoneId).monthValue
    }

    fun getCurrentMonthFirstDay(): String {
        val today = LocalDate.now(koreaZoneId)
        val firstDay = today.withDayOfMonth(1)
        return firstDay.format(yyyyMMddFormatter)
    }

    fun getCurrentMonthLastDay(): String {
        val today = LocalDate.now(koreaZoneId)
        val yearMonth = YearMonth.from(today)
        val lastDay = yearMonth.atEndOfMonth()
        return lastDay.format(yyyyMMddFormatter)
    }
}

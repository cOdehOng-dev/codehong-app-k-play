package com.codehong.app.kplay.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {

    private val koreaZoneId = ZoneId.of("Asia/Seoul")
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    fun getCurrentMonth(): Int {
        return LocalDate.now(koreaZoneId).monthValue
    }

    fun getPreviousMonthFirstDay(): String {
        val today = LocalDate.now(koreaZoneId)
        val previousMonth = today.minusMonths(1)
        return previousMonth.withDayOfMonth(1).format(formatter)
    }

    fun getPreviousMonthLastDay(): String {
        val today = LocalDate.now(koreaZoneId)
        val previousMonth = today.minusMonths(1)
        val yearMonth = YearMonth.from(previousMonth)
        val lastDay = yearMonth.atEndOfMonth()
        return lastDay.format(formatter)
    }

    fun getCurrentMonthLastDay(): String {
        val today = LocalDate.now(koreaZoneId)
        val yearMonth = YearMonth.from(today)
        val lastDay = yearMonth.atEndOfMonth()
        return lastDay.format(formatter)
    }

    fun getToday(): String {
        return LocalDate.now(koreaZoneId).format(formatter)
    }

    fun getOneMonthLater(): String {
        return LocalDate.now(koreaZoneId).plusMonths(1).format(formatter)
    }
}

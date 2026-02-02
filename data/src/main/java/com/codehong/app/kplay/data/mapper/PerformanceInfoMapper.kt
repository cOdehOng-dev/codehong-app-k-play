package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.performance.list.PerformanceItemDto
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.library.util.DtoMapper

object PerformanceInfoMapper : DtoMapper<PerformanceItemDto?, PerformanceInfoItem> {
    override fun asDomain(dto: PerformanceItemDto?): PerformanceInfoItem {
        if (dto == null) return PerformanceInfoItem()

        return PerformanceInfoItem(
            id = dto.id,
            name = dto.name,
            startDate = dto.startDate,
            endDate = dto.endDate,
            placeName = dto.facilityName,
            posterUrl = dto.posterUrl,
            area = dto.area.toShortAreaName(),
            genre = dto.genre,
            openRun = dto.openRun,
            state = dto.state,
            awards = dto.awards?.split("&lt;br&gt;")?.firstOrNull()
        )
    }

    private fun String?.toShortAreaName(): String? {
        if (this.isNullOrBlank()) return this
        return when {
            contains("서울") -> "서울"
            contains("부산") -> "부산"
            contains("대구") -> "대구"
            contains("인천") -> "인천"
            contains("광주") -> "광주"
            contains("대전") -> "대전"
            contains("울산") -> "울산"
            contains("세종") -> "세종"
            contains("경기") -> "경기"
            contains("강원") -> "강원"
            contains("충북") || contains("충청북") -> "충북"
            contains("충남") || contains("충청남") -> "충남"
            contains("전북") || contains("전라북") -> "전북"
            contains("전남") || contains("전라남") -> "전남"
            contains("경북") || contains("경상북") -> "경북"
            contains("경남") || contains("경상남") -> "경남"
            contains("제주") -> "제주"
            else -> this
        }
    }
}
fun PerformanceItemDto?.asDomain() = PerformanceInfoMapper.asDomain(this)
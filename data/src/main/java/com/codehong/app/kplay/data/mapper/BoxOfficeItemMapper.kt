package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeDto
import com.codehong.app.kplay.domain.model.BoxOfficeItem

object BoxOfficeItemMapper : DtoMapper<BoxOfficeDto?, BoxOfficeItem> {
    override fun asDomain(dto: BoxOfficeDto?): BoxOfficeItem {
        if (dto == null) return BoxOfficeItem()

        return BoxOfficeItem(
            category = dto.category,
            rank = dto.rank,
            performanceName = dto.performanceName,
            performancePeriod = dto.performancePeriod,
            performanceCount = dto.performanceCount,
            area = dto.area.toShortAreaName(),
            placeName = dto.placeName,
            seatCount = dto.seatCount,
            posterUrl = dto.posterUrl,
            performanceId = dto.performanceId
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
fun BoxOfficeDto?.asDomain() = BoxOfficeItemMapper.asDomain(this)
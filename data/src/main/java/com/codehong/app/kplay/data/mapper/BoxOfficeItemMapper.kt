package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeDto
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.util.toShortAreaName
import com.codehong.library.util.DtoMapper

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

}
fun BoxOfficeDto?.asDomain() = BoxOfficeItemMapper.asDomain(this)
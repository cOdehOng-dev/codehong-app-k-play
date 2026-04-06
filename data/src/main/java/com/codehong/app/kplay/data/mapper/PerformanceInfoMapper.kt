package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.performance.list.PerformanceItemDto
import com.codehong.app.kplay.domain.model.performance.PerformanceInfoItem
import com.codehong.app.kplay.domain.util.toShortAreaName
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
}
fun PerformanceItemDto?.asDomain() = PerformanceInfoMapper.asDomain(this)
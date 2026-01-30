package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.performance.detail.PerformanceDetailDto
import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite

object PerformanceDetailMapper : DtoMapper<PerformanceDetailDto?, PerformanceDetail> {
    override fun asDomain(dto: PerformanceDetailDto?): PerformanceDetail {
        if (dto == null) return PerformanceDetail()

        return PerformanceDetail(
            id = dto.id,
            name = dto.name,
            startDate = dto.startDate,
            endDate = dto.endDate,
            facilityName = dto.facilityName,
            cast = dto.cast,
            crew = dto.crew,
            runtime = dto.runtime,
            ageLimit = dto.ageLimit,
            hostCompany = dto.hostCompany,
            sponsorCompany = dto.sponsorCompany,
            priceInfo = dto.priceInfo,
            posterUrl = dto.posterUrl,
            description = dto.description,
            area = dto.area,
            genre = dto.genre,
            child = dto.child,
            updateDate = dto.updateDate,
            state = dto.state,
            facilityId = dto.facilityId,
            dateGuidance = dto.dateGuidance,
            imageUrlList = dto.imageUrls,
            ticketSiteList = dto.relates?.map { TicketingSite(it.name, it.url) }
        )
    }
}
fun PerformanceDetailDto?.asDomain() = PerformanceDetailMapper.asDomain(this)
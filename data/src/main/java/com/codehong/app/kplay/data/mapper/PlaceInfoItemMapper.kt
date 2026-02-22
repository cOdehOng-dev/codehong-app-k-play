package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.place.FacilitySummaryDto
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.library.util.DtoMapper

object PlaceInfoItemMapper : DtoMapper<FacilitySummaryDto?, PlaceDetail> {

    override fun asDomain(dto: FacilitySummaryDto?): PlaceDetail {
        if (dto == null) return PlaceDetail()

        return PlaceDetail(
            placeId = dto.mt10Id,
            placeName = dto.fcltyNm
        )
    }
}

fun FacilitySummaryDto?.asDomain() = PlaceInfoItemMapper.asDomain(this)
package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.place.FacilitySummaryDto
import com.codehong.app.kplay.domain.model.place.PlaceInfoItem
import com.codehong.library.util.DtoMapper

object PlaceInfoItemMapper : DtoMapper<FacilitySummaryDto?, PlaceInfoItem> {

    override fun asDomain(dto: FacilitySummaryDto?): PlaceInfoItem {
        if (dto == null) return PlaceInfoItem()

        return PlaceInfoItem(
            placeId = dto.mt10Id,
            placeName = dto.fcltyNm
        )
    }
}

fun FacilitySummaryDto?.asDomain() = PlaceInfoItemMapper.asDomain(this)
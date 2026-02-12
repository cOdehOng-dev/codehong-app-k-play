package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.place.FacilityDto
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.library.util.DtoMapper

object PlaceDetailMapper : DtoMapper<FacilityDto?, PlaceDetail> {

    override fun asDomain(dto: FacilityDto?): PlaceDetail {
        if (dto == null) return PlaceDetail()

        return PlaceDetail(
            placeId = dto.mt10Id,
            placeName = dto.fcltyNm,
            placeAddress = dto.address,
            latitude = dto.latitude,
            longitude = dto.longitude
        )
    }
}
fun FacilityDto?.asDomain() = PlaceDetailMapper.asDomain(this)
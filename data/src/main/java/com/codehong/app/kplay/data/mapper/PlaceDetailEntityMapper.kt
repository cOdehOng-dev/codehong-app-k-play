package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.place.PlaceDetailEntity
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.library.util.DtoMapper

object PlaceDetailEntityMapper : DtoMapper<PlaceDetailEntity?, PlaceDetail> {

    override fun asDomain(dto: PlaceDetailEntity?): PlaceDetail {
        if (dto == null) return PlaceDetail()
        return PlaceDetail(
            placeId = dto.placeId,
            placeName = dto.placeName,
            placeAddress = dto.placeAddress,
            latitude = dto.latitude,
            longitude = dto.longitude
        )
    }

    fun asEntity(keyword: String, domain: PlaceDetail): PlaceDetailEntity {
        return PlaceDetailEntity(
            keyword = keyword,
            placeId = domain.placeId,
            placeName = domain.placeName,
            placeAddress = domain.placeAddress,
            latitude = domain.latitude,
            longitude = domain.longitude
        )
    }
}

fun PlaceDetailEntity?.asDomain() = PlaceDetailEntityMapper.asDomain(this)
fun PlaceDetail.toEntity(keyword: String) = PlaceDetailEntityMapper.asEntity(keyword, this)

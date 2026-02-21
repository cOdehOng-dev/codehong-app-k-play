package com.codehong.app.kplay.data.mapper

import com.codehong.app.kplay.data.model.favorite.FavoritePerformanceEntity
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.library.util.DtoMapper

object FavoritePerformanceMapper : DtoMapper<FavoritePerformanceEntity?, FavoritePerformance> {

    // Entity(Data) → Domain
    override fun asDomain(dto: FavoritePerformanceEntity?): FavoritePerformance {
        if (dto == null) return FavoritePerformance()

        return FavoritePerformance(
            id = dto.id,
            name = dto.name,
            posterUrl = dto.posterUrl,
            startDate = dto.startDate,
            endDate = dto.endDate,
            facilityName = dto.facilityName,
            genre = dto.genre
        )
    }

    // Domain → Entity(Data)
    fun asEntity(domain: FavoritePerformance): FavoritePerformanceEntity {
        return FavoritePerformanceEntity(
            id = domain.id ?: "",
            name = domain.name,
            posterUrl = domain.posterUrl,
            startDate = domain.startDate,
            endDate = domain.endDate,
            facilityName = domain.facilityName,
            genre = domain.genre
        )
    }
}

fun FavoritePerformanceEntity?.asDomain() = FavoritePerformanceMapper.asDomain(this)

fun FavoritePerformance.toEntity() = FavoritePerformanceMapper.asEntity(this)

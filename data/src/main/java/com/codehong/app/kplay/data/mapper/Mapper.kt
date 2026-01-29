package com.codehong.app.kplay.data.mapper

interface EntityMapper<Entity, Domain> {
    fun asEntity(domain: Domain): Entity
    fun asDomain(entity: Entity): Domain
}

interface DtoMapper<Dto, Domain> {
//    fun asDto(domain: Domain): Dto
    fun asDomain(dto: Dto): Domain
}

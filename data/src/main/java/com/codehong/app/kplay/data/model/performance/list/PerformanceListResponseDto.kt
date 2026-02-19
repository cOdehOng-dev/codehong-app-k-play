package com.codehong.app.kplay.data.model.performance.list

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs", strict = false)
data class PerformanceListResponseDto(
    @field:ElementList(
        entry = "db",
        inline = true,
        required = false
    )
    var performances: ArrayList<PerformanceItemDto>? = null
)
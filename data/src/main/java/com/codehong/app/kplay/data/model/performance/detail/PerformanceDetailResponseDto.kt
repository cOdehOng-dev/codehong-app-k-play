package com.codehong.app.kplay.data.model.performance.detail

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs", strict = false)
data class PerformanceDetailResponseDto(

    @field:ElementList(
        entry = "db",
        inline = true,
        required = false
    )
    var performances: ArrayList<PerformanceDetailDto>? = null
)

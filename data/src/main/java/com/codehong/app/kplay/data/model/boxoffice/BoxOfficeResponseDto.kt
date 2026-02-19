package com.codehong.app.kplay.data.model.boxoffice

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boxofs", strict = false)
data class BoxOfficeResponseDto(

    @field:ElementList(
        entry = "boxof",
        inline = true,
        required = false
    )
    var boxOffices: ArrayList<BoxOfficeDto>? = null
)

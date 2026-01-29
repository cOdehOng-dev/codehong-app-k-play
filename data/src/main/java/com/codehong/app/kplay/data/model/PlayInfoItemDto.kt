package com.codehong.app.kplay.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "db", strict = false)
data class PlayInfoItemDto(

    @field:Element(name = "mt20id", required = false)
    var id: String? = null,

    @field:Element(name = "prfnm", required = false)
    var name: String? = null,

    @field:Element(name = "prfpdfrom", required = false)
    var startDate: String? = null,

    @field:Element(name = "prfpdto", required = false)
    var endDate: String? = null,

    @field:Element(name = "fcltynm", required = false)
    var facilityName: String? = null,

    @field:Element(name = "poster", required = false)
    var posterUrl: String? = null,

    @field:Element(name = "area", required = false)
    var area: String? = null,

    @field:Element(name = "genrenm", required = false)
    var genre: String? = null,

    @field:Element(name = "openrun", required = false)
    var openRun: String? = null,

    @field:Element(name = "prfstate", required = false)
    var state: String? = null
)

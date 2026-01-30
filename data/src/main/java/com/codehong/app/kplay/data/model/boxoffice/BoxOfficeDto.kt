package com.codehong.app.kplay.data.model.boxoffice

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "boxof", strict = false)
data class BoxOfficeDto(

    @field:Element(name = "cate", required = false)
    var category: String? = null,

    @field:Element(name = "rnum", required = false)
    var rank: String? = null,

    @field:Element(name = "prfnm", required = false)
    var performanceName: String? = null,

    @field:Element(name = "prfpd", required = false)
    var performancePeriod: String? = null,

    @field:Element(name = "prfdtcnt", required = false)
    var performanceCount: String? = null,

    @field:Element(name = "area", required = false)
    var area: String? = null,

    @field:Element(name = "prfplcnm", required = false)
    var placeName: String? = null,

    @field:Element(name = "seatcnt", required = false)
    var seatCount: String? = null,

    @field:Element(name = "poster", required = false)
    var posterUrl: String? = null,

    @field:Element(name = "mt20id", required = false)
    var performanceId: String? = null
)


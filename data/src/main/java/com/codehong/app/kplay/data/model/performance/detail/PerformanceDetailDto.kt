package com.codehong.app.kplay.data.model.performance.detail

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "db", strict = false)
data class PerformanceDetailDto(

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

    @field:Element(name = "prfcast", required = false)
    var cast: String? = null,

    @field:Element(name = "prfcrew", required = false)
    var crew: String? = null,

    @field:Element(name = "prfruntime", required = false)
    var runtime: String? = null,

    @field:Element(name = "prfage", required = false)
    var ageLimit: String? = null,

    @field:Element(name = "entrpsnmH", required = false)
    var hostCompany: String? = null,

    @field:Element(name = "entrpsnmS", required = false)
    var sponsorCompany: String? = null,

    @field:Element(name = "pcseguidance", required = false)
    var priceInfo: String? = null,

    @field:Element(name = "poster", required = false)
    var posterUrl: String? = null,

    @field:Element(name = "sty", required = false)
    var description: String? = null,

    @field:Element(name = "area", required = false)
    var area: String? = null,

    @field:Element(name = "genrenm", required = false)
    var genre: String? = null,

    @field:Element(name = "openrun", required = false)
    var openRun: String? = null,

    @field:Element(name = "visit", required = false)
    var visit: String? = null,

    @field:Element(name = "child", required = false)
    var child: String? = null,

    @field:Element(name = "daehakro", required = false)
    var daehakro: String? = null,

    @field:Element(name = "festival", required = false)
    var festival: String? = null,

    @field:Element(name = "musicallicense", required = false)
    var musicalLicense: String? = null,

    @field:Element(name = "musicalcreate", required = false)
    var musicalCreate: String? = null,

    @field:Element(name = "updatedate", required = false)
    var updateDate: String? = null,

    @field:Element(name = "prfstate", required = false)
    var state: String? = null,

    @field:Element(name = "mt10id", required = false)
    var facilityId: String? = null,

    @field:Element(name = "dtguidance", required = false)
    var dateGuidance: String? = null,

    // 이미지 리스트
    @field:ElementList(
        name = "styurls",
        entry = "styurl",
        inline = false,
        required = false
    )
    var imageUrls: ArrayList<String>? = null,

    // 예매처 정보
    @field:ElementList(
        name = "relates",
        entry = "relate",
        inline = false,
        required = false
    )
    var relates: ArrayList<RelateDto>? = null
)


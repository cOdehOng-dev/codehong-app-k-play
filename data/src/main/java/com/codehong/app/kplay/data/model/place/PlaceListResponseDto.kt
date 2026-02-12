package com.codehong.app.kplay.data.model.place

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "dbs", strict = false)
data class PlaceListResponseDto(
    // <db> 태그들이 리스트로 들어옵니다.
    @field:ElementList(entry = "db", inline = true, required = false)
    var facilities: List<FacilitySummaryDto>? = null
)

@Root(name = "db", strict = false)
data class FacilitySummaryDto(
    @field:Element(name = "fcltynm", required = false)
    var fcltyNm: String? = null, // 시설명 (고양종합운동장)

    @field:Element(name = "mt10id", required = false)
    var mt10Id: String? = null, // 시설 ID (FC003577)

    @field:Element(name = "mt13cnt", required = false)
    var mt13Cnt: String? = null, // 공연장 수 (4) - 숫자가 아닌 경우 대비 String

    @field:Element(name = "fcltychartr", required = false)
    var fcltyChartr: String? = null, // 시설 특성 (기타(비공연장))

    @field:Element(name = "sidonm", required = false)
    var sidoNm: String? = null, // 시도 명 (경기)

    @field:Element(name = "gugunnm", required = false)
    var gugunNm: String? = null, // 구군 명 (고양시)

    @field:Element(name = "opende", required = false)
    var openDe: String? = null // 개관연도 (2003)
)
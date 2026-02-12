package com.codehong.app.kplay.data.model.place

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

// 최상위 루트 <dbs>
@Root(name = "dbs", strict = false)
data class PlaceDetailResponseDto(
    @field:ElementList(entry = "db", inline = true, required = false)
    var facilities: List<FacilityDto>? = null
)

@Root(name = "db", strict = false)
data class FacilityDto(
    @field:Element(name = "fcltynm", required = false)
    var fcltyNm: String? = null, // 시설명 (고양종합운동장)

    @field:Element(name = "mt10id", required = false)
    var mt10Id: String? = null, // 시설 ID

    @field:Element(name = "mt13cnt", required = false)
    var mt13Cnt: String? = null, // 공연장 수

    @field:Element(name = "fcltychartr", required = false)
    var fcltyChartr: String? = null, // 시설 특성

    @field:Element(name = "opende", required = false)
    var openDe: String? = null, // 개관연도

    @field:Element(name = "seatscale", required = false)
    var seatScale: String? = null, // 객석 수

    @field:Element(name = "telno", required = false)
    var telNo: String? = null, // 전화번호

    @field:Element(name = "relateurl", required = false)
    var relateUrl: String? = null, // 홈페이지

    @field:Element(name = "adres", required = false)
    var address: String? = null, // 주소

    @field:Element(name = "la", required = false)
    var latitude: String? = null, // 위도 (Double 변환 필요)

    @field:Element(name = "lo", required = false)
    var longitude: String? = null, // 경도 (Double 변환 필요)

    // 편의시설 여부 (Y/N)
    @field:Element(name = "restaurant", required = false)
    var hasRestaurant: String? = null,

    @field:Element(name = "cafe", required = false)
    var hasCafe: String? = null,

    @field:Element(name = "store", required = false)
    var hasStore: String? = null,

    @field:Element(name = "nolibang", required = false)
    var hasPlayroom: String? = null,

    @field:Element(name = "suyu", required = false)
    var hasNursingRoom: String? = null,

    @field:Element(name = "parkinglot", required = false)
    var hasParkingLot: String? = null,

    // 베리어 프리 정보 (Y/N)
    @field:Element(name = "parkbarrier", required = false)
    var parkBarrier: String? = null,

    @field:Element(name = "restbarrier", required = false)
    var restBarrier: String? = null,

    @field:Element(name = "runwbarrier", required = false)
    var runwBarrier: String? = null,

    @field:Element(name = "elevbarrier", required = false)
    var elevBarrier: String? = null
)
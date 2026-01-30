package com.codehong.app.kplay.domain.model.performance.detail

data class PerformanceDetail(
    val id: String? = null,
    val name: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val facilityName: String? = null,
    val cast: String? = null,
    val crew: String? = null,
    val runtime: String? = null,
    val ageLimit: String? = null,
    val hostCompany: String? = null,
    val sponsorCompany: String? = null,
    val priceInfo: String? = null,
    val posterUrl: String? = null,
    val description: String? = null,
    val area: String? = null,
    val genre: String? = null,
    val child: String? = null,
    val updateDate: String? = null,
    val state: String? = null,
    val facilityId: String? = null,
    val dateGuidance: String? = null,
    // 이미지 리스트
    val imageUrlList: List<String>? = null,
    // 예매처 정보
    val ticketSiteList: List<TicketingSite>? = null
)


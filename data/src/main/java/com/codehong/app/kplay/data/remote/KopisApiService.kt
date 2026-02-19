package com.codehong.app.kplay.data.remote

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeResponseDto
import com.codehong.app.kplay.data.model.performance.detail.PerformanceDetailResponseDto
import com.codehong.app.kplay.data.model.performance.list.PerformanceListResponseDto
import com.codehong.app.kplay.data.model.place.PlaceDetailResponseDto
import com.codehong.app.kplay.data.model.place.PlaceListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KopisApiService {

    @GET("/openApi/restful/pblprfr")
    suspend fun getPerformanceList(
        @Query("service") service: String,
        @Query("stdate") startDate: String,
        @Query("eddate") endDate: String,
        @Query("cpage") currentPage: String,
        @Query("rows") rowsPerPage: String,
        @Query("prfstate") performanceState: String? = null,
        @Query("signgucode") signGuCode: String? = null,
        @Query("signgucodesub") signGuCodeSub: String? = null,
        @Query("kidstate") kidState: String? = null,
        @Query("shcate") genreCode: String? = null
    ): PerformanceListResponseDto

    @GET("/openApi/restful/pblprfr/{mt20id}")
    suspend fun getPerformanceDetail(
        @Path("mt20id") id: String,
        @Query("service") serviceKey: String,
    ): PerformanceDetailResponseDto

    @GET("/openApi/restful/boxoffice")
    suspend fun getBoxOffice(
        @Query("service") serviceKey: String,
        @Query("stdate") stdate: String,
        @Query("eddate") eddate: String,
        @Query("catecode") catecode: String? = null,
        @Query("area") area: String? = null
    ): BoxOfficeResponseDto

    @GET("/openApi/restful/prffest")
    suspend fun getFestivalList(
        @Query("service") serviceKey: String,
        @Query("stdate") startDate: String,
        @Query("eddate") endDate: String,
        @Query("cpage") currentPage: String,
        @Query("rows") rowsPerPage: String,
        @Query("signgucode") signGuCode: String? = null,
        @Query("signgucodesub") signGuCodeSub: String? = null
    ): PerformanceListResponseDto

    @GET("/openApi/restful/prfawad")
    suspend fun getAwardedPerformanceList(
        @Query("service") serviceKey: String,
        @Query("stdate") startDate: String,
        @Query("eddate") endDate: String,
        @Query("cpage") currentPage: String,
        @Query("rows") rowsPerPage: String,
        @Query("signgucode") signGuCode: String? = null,
        @Query("signgucodesub") signGuCodeSub: String? = null
    ): PerformanceListResponseDto

    @GET("/openApi/restful/prfplc")
    suspend fun searchPlace(
        @Query("service") serviceKey: String,
        @Query("cpage") currentPage: String,
        @Query("rows") rowsPerPage: String,
        @Query("shprfnmfct") keyword: String
    ): PlaceListResponseDto

    @GET("openApi/restful/prfplc/{mt10id}")
    suspend fun getPlaceDetail(
        @Path("mt10id") id: String,
        @Query("service") serviceKey: String,
    ): PlaceDetailResponseDto
}
package com.codehong.app.kplay.data.remote

import com.codehong.app.kplay.data.model.boxoffice.BoxOfficeResponse
import com.codehong.app.kplay.data.model.performance.detail.PerformanceDetailResponse
import com.codehong.app.kplay.data.model.performance.list.PerformanceListResponse
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
    ): PerformanceListResponse

    @GET("/openApi/restful/pblprfr/{mt20id}")
    suspend fun getPerformanceDetail(
        @Path("mt20id") id: String,
        @Query("service") serviceKey: String,
    ): PerformanceDetailResponse

    @GET("/openApi/restful/boxoffice")
    suspend fun getBoxOffice(
        @Query("service") serviceKey: String,
        @Query("stdate") stdate: String,
        @Query("eddate") eddate: String,
        @Query("catecode") catecode: String? = null,
        @Query("area") area: String? = null
    ): BoxOfficeResponse

    @GET("/openApi/restful/prffest")
    suspend fun getFestivalList(
        @Query("service") serviceKey: String,
        @Query("stdate") startDate: String,
        @Query("eddate") endDate: String,
        @Query("cpage") currentPage: String,
        @Query("rows") rowsPerPage: String,
        @Query("signgucode") signGuCode: String? = null,
        @Query("signgucodesub") signGuCodeSub: String? = null
    ): PerformanceListResponse
}
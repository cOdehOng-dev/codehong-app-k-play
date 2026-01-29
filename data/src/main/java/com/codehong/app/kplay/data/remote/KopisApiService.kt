package com.codehong.app.kplay.data.remote

import com.codehong.app.kplay.data.model.PerformanceListResponseDto
import retrofit2.http.GET
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
        @Query("signgucode") cityCode: String? = null,
        @Query("signgucodesub") signGuCodeSub: String? = null,
        @Query("kidstate") kidState: String? = null
    ): PerformanceListResponseDto
}
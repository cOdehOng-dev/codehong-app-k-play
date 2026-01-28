package com.codehong.app.kplay

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class KplayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)
    }
}
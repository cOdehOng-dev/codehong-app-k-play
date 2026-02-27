package com.codehong.app.kplay

import android.app.Application
import com.codehong.library.debugtool.log.TimberConfig
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.network.NetworkConfig
import com.codehong.library.network.NetworkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KplayApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)

        TimberUtil.init(
            config = TimberConfig.Builder()
                .packageName(this.packageName)
                .tag("Kplay앱")
                .isEnabled(true)
                .build()
        )

        NetworkManager.init(
            this,
            NetworkConfig.Builder()
                .isEnabledLogging(true)
                .isEnabledLoggingLineBreak(true)
                .isEnabledLoggingJsonFormatter(true)
                .build()
        )
    }
}
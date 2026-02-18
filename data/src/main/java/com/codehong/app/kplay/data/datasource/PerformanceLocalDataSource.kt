package com.codehong.app.kplay.data.datasource

import com.codehong.app.kplay.domain.Consts
import javax.inject.Inject

class PerformanceLocalDataSource @Inject constructor(
    private val pref: PrefManager
) {

    fun setMyLocation(
        myLocation: String?
    ) {
        pref.putString(
            name = Consts.PREF_NAME,
            key = Consts.KEY_SIGN_GU_CODE,
            value = myLocation
        )
    }

    fun getMyLocation(): String? {
        return pref.getString(
            name = Consts.PREF_NAME,
            key = Consts.KEY_SIGN_GU_CODE,
            def = ""
        )
    }
}
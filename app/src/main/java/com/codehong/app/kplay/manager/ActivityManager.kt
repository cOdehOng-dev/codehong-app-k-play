package com.codehong.app.kplay.manager

import android.content.Context
import android.content.Intent
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.ui.performance.detail.PerformanceDetailActivity

object ActivityManager {

    fun openPerformanceDetail(
        context: Context?,
        id: String?
    ) {
        if (context == null || id.isNullOrEmpty()) {
            return
        }

        val intent = Intent(context, PerformanceDetailActivity::class.java).apply {
            putExtra(Consts.PERFORMANCE_ID, id)
        }
        context.startActivity(intent)
    }
}
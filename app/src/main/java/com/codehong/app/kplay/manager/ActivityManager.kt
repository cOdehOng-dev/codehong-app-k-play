package com.codehong.app.kplay.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.ui.genre.GenreRankListActivity
import com.codehong.app.kplay.ui.localtab.LocalTabListActivity
import com.codehong.app.kplay.ui.localtab.LocalTabType
import com.codehong.app.kplay.ui.performance.detail.PerformanceDetailActivity
import com.codehong.library.widget.R

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

    fun openGenreList(
        context: Context?,
        genreCode: String?
    ) {
        if (context == null || genreCode.isNullOrEmpty()) {
            return
        }

        val intent = Intent(context, LocalTabListActivity::class.java).apply {
            putExtra(Consts.EXTRA_GENRE_CODE, genreCode)
            putExtra(Consts.EXTRA_LOCAL_TAB_TYPE, LocalTabType.GENRE.type)
        }
        context.startActivity(intent)
    }

    fun openFestivalList(
        context: Context?,
        regionCode: String?
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, LocalTabListActivity::class.java).apply {
            putExtra(Consts.EXTRA_REGION_CODE, regionCode)
            putExtra(Consts.EXTRA_LOCAL_TAB_TYPE, LocalTabType.FESTIVAL.type)
        }
        context.startActivity(intent)
    }

    fun openLocalList(
        context: Context?,
        regionCode: String?
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, LocalTabListActivity::class.java).apply {
            putExtra(Consts.EXTRA_REGION_CODE, regionCode)
            putExtra(Consts.EXTRA_LOCAL_TAB_TYPE, LocalTabType.REGION.type)
        }
        context.startActivity(intent)
    }

    fun openGenreRankList(
        context: Context?,
        genreCode: String?
    ) {
        if (context == null || genreCode.isNullOrEmpty()) {
            return
        }

        val intent = Intent(context, GenreRankListActivity::class.java).apply {
            putExtra(Consts.EXTRA_GENRE_CODE, genreCode)
        }
        context.startActivity(intent)
    }



    fun openAwardList(
        context: Context?,
        regionCode: String?
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, LocalTabListActivity::class.java).apply {
            putExtra(Consts.EXTRA_REGION_CODE, regionCode)
            putExtra(Consts.EXTRA_LOCAL_TAB_TYPE, LocalTabType.AWARD.type)
        }
        context.startActivity(intent)
    }

    fun openCustomTab(
        context: Context?,
        url: String?
    ) {
        if (context == null || url.isNullOrEmpty()) {
            return
        }

        val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.honglib_color_ff8224))
            .setShowTitle(true)
            .setStartAnimations(context, R.anim.honglib_popup_in, 0)
            .setExitAnimations(context, 0, R.anim.honglib_popup_out)
            .build()

        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}
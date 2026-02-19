package com.codehong.app.kplay.manager

import android.content.Context
import android.content.Intent
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.ui.local.LocalListActivity
import com.codehong.app.kplay.ui.award.AwardListActivity
import com.codehong.app.kplay.ui.festival.FestivalListActivity
import com.codehong.app.kplay.ui.genre.list.GenreListActivity
import com.codehong.app.kplay.ui.genre.rank.GenreRankListActivity
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

    fun openGenreList(
        context: Context?,
        genreCode: String?
    ) {
        if (context == null || genreCode.isNullOrEmpty()) {
            return
        }

        val intent = Intent(context, GenreListActivity::class.java).apply {
            putExtra(Consts.EXTRA_GENRE_CODE, genreCode)
        }
        context.startActivity(intent)
    }

    fun openFestivalList(
        context: Context?,
        signGuCode: String? = null
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, FestivalListActivity::class.java).apply {
            signGuCode?.let { putExtra(Consts.SIGN_GU_CODE, it) }
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

    fun openLocalList(
        context: Context?,
        signGuCode: String? = null
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, LocalListActivity::class.java).apply {
            signGuCode?.let { putExtra(Consts.EXTRA_SIGN_GU_CODE, it) }
        }
        context.startActivity(intent)
    }

    fun openAwardList(
        context: Context?
    ) {
        if (context == null) {
            return
        }

        val intent = Intent(context, AwardListActivity::class.java)
        context.startActivity(intent)
    }

    fun openExternalUrl(
        context: Context?,
        url: String?
    ) {
        if (context == null || url.isNullOrEmpty()) {
            return
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = android.net.Uri.parse(url)
        }
        context.startActivity(intent)
    }
}
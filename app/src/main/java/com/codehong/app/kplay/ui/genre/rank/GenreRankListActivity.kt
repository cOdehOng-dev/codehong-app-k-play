package com.codehong.app.kplay.ui.genre.rank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreRankListActivity : ComponentActivity() {

    private val viewModel by viewModels<GenreRankListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is GenreRankListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@GenreRankListActivity,
                                id = effect.performanceId
                            )
                        }
                    }
                }
            }

            GenreRankListScreen(
                onClickBack = { finish() }
            )
        }
    }
}

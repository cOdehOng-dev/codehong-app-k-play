package com.codehong.app.kplay.ui.genre.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreListActivity : ComponentActivity() {

    private val viewModel by viewModels<GenreListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is GenreListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@GenreListActivity,
                                id = effect.performanceId
                            )
                        }
                    }
                }
            }

            GenreListScreen(
                onBackClick = { finish() }
            )
        }
    }
}

package com.codehong.app.kplay.ui.localtab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalTabListActivity : ComponentActivity() {

    private val viewModel by viewModels<LocalTabListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is LocalTabListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@LocalTabListActivity,
                                id = effect.performanceId
                            )
                        }
                    }
                }
            }

            LocalTabListScreen(
                onClickBack = { finish() }
            )
        }
    }
}

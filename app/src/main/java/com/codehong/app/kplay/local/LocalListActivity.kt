package com.codehong.app.kplay.local

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalListActivity : ComponentActivity() {

    private val viewModel by viewModels<LocalListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is LocalListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@LocalListActivity,
                                id = effect.performanceId
                            )
                        }
                    }
                }
            }

            LocalListScreen(
                onBackClick = { finish() }
            )
        }
    }
}
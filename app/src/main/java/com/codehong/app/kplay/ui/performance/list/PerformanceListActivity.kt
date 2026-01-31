package com.codehong.app.kplay.ui.performance.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformanceListActivity : ComponentActivity() {

    private val viewModel by viewModels<PerformanceListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is PerformanceListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@PerformanceListActivity,
                                id = effect.performanceId
                            )
                        }
                        is PerformanceListEffect.ShowDatePicker -> {
                            // TODO: 달력 다이얼로그 표시
                            // 날짜 선택 후 viewModel.setEvent(PerformanceListEvent.OnDateSelected(startDate, endDate)) 호출
                        }
                    }
                }
            }

            PerformanceListScreen(
                onBackClick = { finish() }
            )
        }
    }
}

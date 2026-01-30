package com.codehong.app.kplay.ui.festival

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalListActivity : ComponentActivity() {

    private val viewModel by viewModels<FestivalListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is FestivalListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@FestivalListActivity,
                                id = effect.performanceId
                            )
                        }
                        is FestivalListEffect.ShowDatePicker -> {
                            // TODO: 달력 다이얼로그 표시
                            // 날짜 선택 후 viewModel.setEvent(FestivalListEvent.OnDateSelected(startDate, endDate)) 호출
                        }
                    }
                }
            }

            FestivalListScreen(
                onBackClick = { finish() }
            )
        }
    }
}

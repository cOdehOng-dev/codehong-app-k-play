package com.codehong.app.kplay.ui.award

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AwardListActivity : ComponentActivity() {

    private val viewModel by viewModels<AwardListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is AwardListEffect.NavigateToDetail -> {
                            ActivityManager.openPerformanceDetail(
                                context = this@AwardListActivity,
                                id = effect.performanceId
                            )
                        }
                        is AwardListEffect.ShowDatePicker -> {
                            // TODO: 달력 다이얼로그 표시
                            // 날짜 선택 후 viewModel.setEvent(AwardListEvent.OnDateSelected(startDate, endDate)) 호출
                        }
                    }
                }
            }

            AwardListScreen(
                onBackClick = { finish() }
            )
        }
    }
}

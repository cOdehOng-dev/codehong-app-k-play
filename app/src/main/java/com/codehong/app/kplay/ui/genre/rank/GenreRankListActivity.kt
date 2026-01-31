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
                        is GenreRankListEffect.ShowDatePicker -> {
                            // TODO: 달력 다이얼로그 표시
                            // 날짜 선택 후 viewModel.setEvent(GenreRankListEvent.OnDateSelected(startDate, endDate)) 호출
                        }
                    }
                }
            }

            GenreRankListScreen(
                onBackClick = { finish() }
            )
        }
    }
}

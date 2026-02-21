package com.codehong.app.kplay.ui.performance.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.manager.ActivityManager
import com.codehong.app.kplay.ui.theme.CodehongappkplayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PerformanceDetailActivity : ComponentActivity() {

    private val viewModel: PerformanceDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val performanceId = intent.getStringExtra(Consts.PERFORMANCE_ID) ?: ""

        setContent {
            CodehongappkplayTheme {
                val state by viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.callPerformanceDetailApi(performanceId)
                }

                LaunchedEffect(Unit) {
                    viewModel.effect.collectLatest { effect ->
                        when (effect) {
                            is PerformanceDetailEffect.NavigateBack -> {
                                finish()
                            }
                            is PerformanceDetailEffect.OpenBookingPage -> {
                                val siteList = effect.relates
                                if (siteList.isEmpty()) return@collectLatest
                                if (siteList.size > 1) {
                                    // 예매처 선택 다이얼로그 표시
                                    viewModel.callReservationSiteListPicker(siteList)

                                } else {
                                    // 단일 예매처 바로 이동
                                    viewModel.setEvent(PerformanceDetailEvent.OnBookingSiteClick(siteList.firstOrNull()?.url))
                                }
                            }

                            is PerformanceDetailEffect.OpenBookingSitePage -> {
                                ActivityManager.openExternalUrl(this@PerformanceDetailActivity, effect.site)
                            }

                            is PerformanceDetailEffect.ShowFavoriteToast -> {
                                val message = if (effect.isAdded) "찜하기에 추가했어요." else "찜하기에서 삭제했어요."
                                Toast.makeText(this@PerformanceDetailActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                PerformanceDetailScreen(
                    state = state,
                    onEvent = viewModel::setEvent
                )
            }
        }
    }
}
